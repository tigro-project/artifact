##
## Copyright 2025 The Tigro Authors.
##

from tor_proxy.onion import *
from tor_proxy.onionstart import OnionStart

import sys
import stem


def tor_proxy():
    onion = Onion()
    try:
        onion.connect()
    except (
        TorTooOld,
        TorErrorInvalidSetting,
        TorErrorAutomatic,
        TorErrorSocketPort,
        TorErrorSocketFile,
        TorErrorMissingPassword,
        TorErrorUnreadableCookieFile,
        TorErrorAuthError,
        TorErrorProtocolError,
        BundledTorNotSupported,
        BundledTorTimeout,
    ) as e:
        sys.exit(e.args[0])
    except KeyboardInterrupt:
        print("")
        sys.exit()

    # Start the onionshare app
    try:
        app_tor = OnionStart(onion)
        # app_tor.set_stealth(stealth)
        app_tor.start_onion_service()
        proxy_port = app_tor.get_tor_socks_port()

        controller = app_tor.onion.c

        return (proxy_port[1], controller)

    except KeyboardInterrupt:
        print("")
        sys.exit()


import requests
import time
import uuid

import base64

from dataclasses import dataclass

from arca import crypto

import os

import abc

from typing import Dict, List, Set, Optional

ProxyConfig = Dict[str, str]

from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import dh
from cryptography.hazmat.primitives.kdf.hkdf import HKDF
from cryptography.hazmat.primitives.serialization import (
    load_pem_parameters,
)

print("Reading DH parameters...")
parameters = None
with open("dh-parameters.pem", "rb") as param_bytes:
    parameters = load_pem_parameters(param_bytes.read())


class TorProxyGenerator:
    def __init__(self, tor_proxy_uri: str = "socks5h://127.0.0.1"):
        self.tor_proxy_uri = tor_proxy_uri

        self.regen_settings()

    def regen_settings(self):
        print("FORCE REGEN")
        port, controller = tor_proxy()
        http_proxy = f"{self.tor_proxy_uri}:{port}"
        https_proxy = f"{self.tor_proxy_uri}:{port}"
        self.proxy_config = {
            "http": http_proxy,
            "https": https_proxy,
        }
        self.controller = controller


class TigroServerProxy(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def test(self): ...

    @abc.abstractmethod
    def create_box(self): ...

    @abc.abstractmethod
    def drop_mail(self, box_id: str, label: str, mail: str): ...

    @abc.abstractmethod
    def get_mail(self, box_id: str, label: str) -> bytes: ...

    @abc.abstractmethod
    def delete_mail(self, box_id: str, label: str): ...


class StubTigroServerProxy(TigroServerProxy):
    def __init__(self, server_uri: str):
        self.server_uri: str = server_uri

    def test(self):
        r = requests.get(
            f"https://api.ipify.org",
        )
        print(r.text)

    def create_box(self):
        r = requests.post(
            f"{self.server_uri}/create_box",
        )
        self._cleanup_circuit()

    def multi_drop_mail(self, box_ids, labels, mails):
        r = requests.post(
            f"{self.server_uri}/drop_mails",
            json={"po_id": box_ids, "label": labels, "mail": mails},
        )
        assert r.json()["success"]

    def multi_get_mail(self, box_ids, labels):
        r = requests.get(
            f"{self.server_uri}/get_mails",
            json={"po_id": box_ids, "label": labels},
        )
        return r.json()["mails"]

    def multi_delete_mail(self, box_ids, labels):
        r = requests.delete(
            f"{self.server_uri}/delete_mails",
            json={"po_id": box_ids, "label": labels},
        )
        assert r.json()["success"]

    def drop_mail(self, box_id: str, label: str, mail: str):
        r = requests.post(
            f"{self.server_uri}/drop_mail/{box_id}/{label}",
            data={"mail": mail},
        )
        assert r.json()["success"]

    def get_mail(self, box_id: str, label: str) -> bytes:
        r = requests.get(
            f"{self.server_uri}/get_mail/{box_id}/{label}",
        )
        return r.json()["mail"]

    def delete_mail(self, box_id: str, label: str):
        r = requests.delete(
            f"{self.server_uri}/delete_mail/{box_id}/{label}",
        )
        assert r.json()["success"]


class NewCircuitEveryTimeTigroServerProxy(TigroServerProxy):
    def __init__(self, server_uri: str, proxy_generator: TorProxyGenerator):
        self.server_uri: str = server_uri
        self.proxy_generator: TorProxyGenerator = proxy_generator
        self.circuit_id = None
        self.attacher = None

        self.proxy_generator.controller.set_conf(
            "__LeaveStreamsUnattached", "1"
        )  # leave stream management to us

    def test(self):
        self._setup_circuit()
        r = requests.get(
            f"https://api.ipify.org",
            proxies=self.proxy_generator.proxy_config,
        )
        self._cleanup_circuit()
        print(r.text)

    def create_box(self):
        self._setup_circuit()
        r = requests.post(
            f"{self.server_uri}/create_box",
            proxies=self.proxy_generator.proxy_config,
        )
        self._cleanup_circuit()
        return r.json()["box_id"]

    def drop_mail(self, box_id: str, label: str, mail: str):
        self._setup_circuit()
        r = requests.post(
            f"{self.server_uri}/drop_mail/{box_id}/{label}",
            proxies=self.proxy_generator.proxy_config,
            data={"mail": mail},
        )
        self._cleanup_circuit()
        assert r.json()["success"]

    def get_mail(self, box_id: str, label: str) -> bytes:
        self._setup_circuit()
        r = requests.get(
            f"{self.server_uri}/get_mail/{box_id}/{label}",
            proxies=self.proxy_generator.proxy_config,
        )
        self._cleanup_circuit()
        return r.json()["mail"]

    def delete_mail(self, box_id: str, label: str):
        self._setup_circuit()
        r = requests.delete(
            f"{self.server_uri}/delete_mail/{box_id}/{label}",
            proxies=self.proxy_generator.proxy_config,
        )
        self._cleanup_circuit()
        assert r.json()["success"]

    def _setup_circuit(self) -> None:
        num_retries = 0
        while self.circuit_id is None:
            try:
                self.circuit_id = self.proxy_generator.controller.new_circuit(
                    await_build=True
                )
            except:
                num_retries += 1
                if num_retries > 3:
                    print("too many retries, returning")
                    return
                else:
                    print("retrying circuit build")

        print(f"New Circuit {self.circuit_id}")

        def attach_stream(stream):
            if stream.status == "NEW":
                self.proxy_generator.controller.attach_stream(
                    stream.id, self.circuit_id
                )

        self.attacher = attach_stream

        self.proxy_generator.controller.add_event_listener(
            self.attacher, stem.control.EventType.STREAM
        )

    def _cleanup_circuit(self) -> None:
        self.proxy_generator.controller.close_circuit(self.circuit_id)
        self.proxy_generator.controller.remove_event_listener(self.attacher)
        self.proxy_generator.controller.reset_conf("__LeaveStreamsUnattached")
        self.attacher = None
        self.circuit_id = None


class CircuitPerBoxTigroServerProxy(TigroServerProxy):
    def __init__(self, server_uri: str, proxy_generator: TorProxyGenerator):
        self.server_uri: str = server_uri
        self.proxy_generator: TorProxyGenerator = proxy_generator

        self.box_id_circuit_map = {}
        self.circuit_id = None
        self.attacher = None

        self.proxy_generator.controller.set_conf(
            "__LeaveStreamsUnattached", "1"
        )  # leave stream management to us

    def test(self):
        self._setup_circuit("")
        r = requests.get(
            f"https://api.ipify.org",
            proxies=self.proxy_generator.proxy_config,
        )
        self._cleanup_circuit()
        print(r.text)

    def create_box(self):
        self._setup_circuit("")
        r = requests.post(
            f"{self.server_uri}/create_box",
            proxies=self.proxy_generator.proxy_config,
        )
        self._cleanup_circuit()
        return r.json()["box_id"]

    def drop_mail(self, box_id: str, label: str, mail: str):
        self._setup_circuit(box_id)
        while True:
            try:
                r = requests.post(
                    f"{self.server_uri}/drop_mail/{box_id}/{label}",
                    proxies=self.proxy_generator.proxy_config,
                    data={"mail": mail},
                )
                self._cleanup_circuit()
                assert r.json()["success"]
                return
            except:
                self._setup_circuit(box_id, force_new=True)

    def get_mail(self, box_id: str, label: str) -> bytes:
        self._setup_circuit(box_id)
        while True:
            try:
                r = requests.get(
                    f"{self.server_uri}/get_mail/{box_id}/{label}",
                    proxies=self.proxy_generator.proxy_config,
                )
                self._cleanup_circuit()
                return r.json()["mail"]
            except:
                self._setup_circuit(box_id, force_new=True)

    def delete_mail(self, box_id: str, label: str):
        self._setup_circuit(box_id)
        while True:
            try:
                r = requests.delete(
                    f"{self.server_uri}/delete_mail/{box_id}/{label}",
                    proxies=self.proxy_generator.proxy_config,
                )
                assert r.json()["success"]
                self._cleanup_circuit()
                return
            except:
                self._setup_circuit(box_id, force_new=True)

    def _setup_circuit(self, box_id, force_new=False) -> None:
        print("SETUP")
        num_retries = 0
        while (box_id not in self.box_id_circuit_map) or force_new:
            try:
                self.box_id_circuit_map[box_id] = (
                    self.proxy_generator.controller.new_circuit(
                        await_build=True, timeout=10
                    )
                )
            except:
                num_retries += 1
                if num_retries > 3:
                    print("too many retries, returning")
                    return
                else:
                    print("retrying circuit build")

        self.proxy_generator.controller.get_circuit

        print(f"Using Circuit {self.box_id_circuit_map[box_id]}")

        def attach_stream(stream):
            if stream.status == "NEW":
                self.proxy_generator.controller.attach_stream(
                    stream.id, self.box_id_circuit_map[box_id]
                )

        self.attacher = attach_stream

        self.proxy_generator.controller.add_event_listener(
            self.attacher, stem.control.EventType.STREAM
        )

    def _cleanup_circuit(self) -> None:
        self.proxy_generator.controller.remove_event_listener(self.attacher)
        self.attacher = None
        self.circuit_id = None


class SingleCircuitTigroServerProxy(TigroServerProxy):
    def __init__(self, server_uri: str, proxy_generator: TorProxyGenerator):
        self.server_uri: str = server_uri
        self.proxy_generator: TorProxyGenerator = proxy_generator
        self.proxy_generator.controller.reset_conf("__LeaveStreamsUnattached")

    def test(self):
        r = requests.get(
            f"https://api.ipify.org",
            proxies=self.proxy_generator.proxy_config,
        )
        print(r.text)

    def create_box(self):
        r = requests.post(
            f"{self.server_uri}/create_box",
            proxies=self.proxy_generator.proxy_config,
        )
        return r.json()["box_id"]

    def multi_drop_mail(self, box_ids, labels, mails):
        r = requests.post(
            f"{self.server_uri}/drop_mails",
            proxies=self.proxy_generator.proxy_config,
            json={"po_id": box_ids, "label": labels, "mail": mails},
        )
        assert r.json()["success"]

    def multi_get_mail(self, box_ids, labels):
        r = requests.get(
            f"{self.server_uri}/get_mails",
            proxies=self.proxy_generator.proxy_config,
            json={"po_id": box_ids, "label": labels},
        )
        return r.json()["mails"]

    def multi_delete_mail(self, box_ids, labels):
        r = requests.delete(
            f"{self.server_uri}/delete_mails",
            proxies=self.proxy_generator.proxy_config,
            json={"po_id": box_ids, "label": labels},
        )
        assert r.json()["success"]

    def drop_mail(self, box_id: str, label: str, mail: str):
        r = requests.post(
            f"{self.server_uri}/drop_mail/{box_id}/{label}",
            proxies=self.proxy_generator.proxy_config,
            data={"mail": mail},
        )
        assert r.json()["success"]

    def get_mail(self, box_id: str, label: str) -> bytes:
        r = requests.get(
            f"{self.server_uri}/get_mail/{box_id}/{label}",
            proxies=self.proxy_generator.proxy_config,
        )
        return r.json()["mail"]

    def delete_mail(self, box_id: str, label: str):
        r = requests.delete(
            f"{self.server_uri}/delete_mail/{box_id}/{label}",
            proxies=self.proxy_generator.proxy_config,
        )
        assert r.json()["success"]


@dataclass
class BoxState:
    box_key: bytes
    box_id: str


class EncryptedBoxBank:
    HMAC_PURPOSE = "mac"
    ENCRYPT_PURPOSE = "enc"

    def __init__(self, tigro_server_proxy: TigroServerProxy):
        self.tigro_server_proxy = tigro_server_proxy

    def create_box(self):
        return str(uuid.uuid4())
        # return self.tigro_server_proxy.create_box()

    def drop_mail(self, box_key: bytes, box_id: str, oid: str, annotation: str) -> None:
        tkn = self._token(box_key, oid)
        ct = self._encrypt(box_key, annotation)
        self.tigro_server_proxy.drop_mail(box_id=box_id, label=tkn, mail=ct)

    def multi_drop_mail(
        self, box_states: List[BoxState], oid: str, annotation: str
    ) -> None:
        box_ids = []
        labels = []
        mails = []
        for state in box_states:
            tkn = self._token(state.box_key, oid)
            ct = self._encrypt(state.box_key, annotation)
            box_ids.append(state.box_id)
            labels.append(tkn)
            mails.append(ct)

        self.tigro_server_proxy.multi_drop_mail(box_ids, labels, mails)

    def get_mail(self, box_key: bytes, box_id: str, oid: str) -> Optional[str]:
        tkn = self._token(box_key, oid)
        ct = self.tigro_server_proxy.get_mail(box_id=box_id, label=tkn)
        if ct is None:
            return None

        return self._decrypt(box_key, ct)

    def multi_get_mail(self, box_states: List[BoxState], oid: str) -> List[str]:
        box_ids = []
        labels = []
        for state in box_states:
            tkn = self._token(state.box_key, oid)
            box_ids.append(state.box_id)
            labels.append(tkn)

        cts = self.tigro_server_proxy.multi_get_mail(box_ids, labels)
        pts = []
        for box_state, ct in zip(box_states, cts):
            if ct is not None:
                pts.append(self._decrypt(box_state.box_key, ct))
        return pts

    def delete_mail(self, box_key: bytes, box_id: str, oid: str) -> None:
        tkn = self._token(box_key, oid)
        self.tigro_server_proxy.delete_mail(box_id=box_id, label=tkn)

    def multi_delete_mail(self, box_states: List[BoxState], oid: str) -> None:
        box_ids = []
        labels = []
        for state in box_states:
            tkn = self._token(state.box_key, oid)
            box_ids.append(state.box_id)
            labels.append(tkn)

        self.tigro_server_proxy.multi_delete_mail(box_ids, labels)

    def _token(self, box_key: bytes, oid: str) -> str:
        hmac_key = crypto.HashKDF(box_key, EncryptedBoxBank.HMAC_PURPOSE)
        return base64.urlsafe_b64encode(crypto.HMAC(hmac_key, oid.encode())).decode()

    def _encrypt(self, box_key: bytes, annotation: str) -> bytes:
        encrypt_key = crypto.HashKDF(box_key, EncryptedBoxBank.ENCRYPT_PURPOSE)
        return base64.b64encode(
            crypto.SymmetricEncrypt(encrypt_key, annotation.encode())
        ).decode()

    def _decrypt(self, box_key: bytes, ciphertext: str) -> bytes:
        encrypt_key = crypto.HashKDF(box_key, EncryptedBoxBank.ENCRYPT_PURPOSE)
        return crypto.SymmetricDecrypt(
            encrypt_key, base64.b64decode(ciphertext.encode())
        ).decode()


class TigroClient:
    def __init__(
        self, encrypted_box_bank: EncryptedBoxBank, id: str, batch: bool = False
    ):
        self.encrypted_box_bank: EncryptedBoxBank = encrypted_box_bank
        self.id: str = id
        self.contact_states: Dict[str, BoxState] = {}
        self.batch = batch

    def init_handshake(self) -> None:
        self.private_key = parameters.generate_private_key()

    def get_public_key(self) -> dh.DHPublicKey:
        return self.private_key.public_key()

    def handshake(self, peer_id: str, peer_public_key: dh.DHPublicKey) -> BoxState:
        shared_key = self.private_key.exchange(peer_public_key)
        derived_key = HKDF(
            algorithm=hashes.SHA256(),
            length=32,
            salt=None,
            info=b"handshake data",
        ).derive(shared_key)

        box_id = self.encrypted_box_bank.create_box()
        new_box_state = BoxState(box_key=derived_key, box_id=box_id)
        self.contact_states[peer_id] = new_box_state

        return new_box_state

    def finish_handshake(self, peer_id: str, new_box_state: BoxState) -> None:
        self.contact_states[peer_id] = new_box_state

    def annotate(self, oid: str, annotation: str, authlist: List[str]) -> None:
        if self.batch:
            print("BATCH")
            self.encrypted_box_bank.multi_drop_mail(
                box_states=list(
                    map(lambda peer_id: self.contact_states[peer_id], authlist)
                ),
                oid=oid,
                annotation=annotation,
            )
        else:
            for peer_id in authlist:
                peer_box_state = self.contact_states[peer_id]
                self.encrypted_box_bank.drop_mail(
                    box_key=peer_box_state.box_key,
                    box_id=peer_box_state.box_id,
                    oid=oid,
                    annotation=annotation,
                )

    def get_annotation(self, oid: str) -> Set[str]:
        if self.batch:
            print("BATCH")
            self.encrypted_box_bank.multi_get_mail(
                box_states=list(
                    map(
                        lambda peer_id: self.contact_states[peer_id],
                        self.contact_states.keys(),
                    )
                ),
                oid=oid,
            )
        else:
            annotations = set()
            for _, peer_box_state in self.contact_states.items():
                result = self.encrypted_box_bank.get_mail(
                    box_key=peer_box_state.box_key,
                    box_id=peer_box_state.box_id,
                    oid=oid,
                )
                if result is not None:
                    annotations.add(result)
            return annotations

    def delete_annotation(self, oid: str, authlist: List[str]) -> None:
        if self.batch:
            print("BATCH")
            self.encrypted_box_bank.multi_delete_mail(
                box_states=list(
                    map(lambda peer_id: self.contact_states[peer_id], authlist)
                ),
                oid=oid,
            )
        else:

            for peer_id in authlist:
                peer_box_state = self.contact_states[peer_id]
                self.encrypted_box_bank.delete_mail(
                    box_key=peer_box_state.box_key,
                    box_id=peer_box_state.box_id,
                    oid=oid,
                )


class StubTorProxyGenerator:
    def get_tor_proxy_config(self) -> ProxyConfig:
        return {}


def perform_handshake(client_a: TigroClient, client_b: TigroClient):
    print(f"Handshake between {client_a.id} and {client_b.id}...", end="", flush=True)
    client_a.init_handshake()
    client_b.init_handshake()
    box_state = client_a.handshake(
        peer_id=client_b.id, peer_public_key=client_b.get_public_key()
    )
    client_b.finish_handshake(peer_id=client_a.id, new_box_state=box_state)
    print("done.")
