##
## Copyright 2025 The Tigro Authors.
##

from client import (
    TorProxyGenerator,
    SingleCircuitTigroServerProxy,
    CircuitPerBoxTigroServerProxy,
    TigroServerProxy,
    EncryptedBoxBank,
    TigroClient,
    perform_handshake,
    StubTigroServerProxy,
)

from typing import List

import random
import string
import time
import pprint
import sys
import tor_proxy.onion

TIGRO_SERVER_URI = "UPDATE_THIS_URI"

SLEEP_TIME = 1.2
NUMBER_OF_CLIENTS = 25
TOR_PROXY_URI = "socks5h://127.0.0.1"
STUB_PROXY = StubTigroServerProxy(server_uri=TIGRO_SERVER_URI)


def tor_proxy():
    # Start the Onion object
    onion = tor_proxy.onion.Onion()
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
    except KeyboardInterrupt:
        print("")
        sys.exit()

    return proxy_port[1]


def benchmark(tigro_server_proxy: TigroServerProxy, batch: bool):
    print("Starting benchmark.")
    print("Generating clients...")

    clients: List[TigroClient] = []
    for i in range(NUMBER_OF_CLIENTS):
        if i == 0:
            clients.append(
                TigroClient(
                    encrypted_box_bank=EncryptedBoxBank(
                        tigro_server_proxy=tigro_server_proxy
                    ),
                    id=f"C{i}",
                    batch=batch,
                )
            )
        else:
            clients.append(
                TigroClient(
                    encrypted_box_bank=EncryptedBoxBank(tigro_server_proxy=STUB_PROXY),
                    id=f"C{i}",
                    batch=True,
                )
            )

    print("Performing handshakes between C0 and all clients...")
    for j in range(1, NUMBER_OF_CLIENTS):
        perform_handshake(clients[0], clients[j])

    def generate_random_string(size: int) -> str:
        return "".join(random.choices(string.ascii_uppercase + string.digits, k=size))

    first_contact_execution_times = []
    for client_id in range(1, NUMBER_OF_CLIENTS):
        print(f"Making first contact with C{client_id}...")
        authlist = [clients[client_id].id]
        annotation = generate_random_string(1024)
        oid = generate_random_string(8)

        start = time.perf_counter_ns()
        clients[0].annotate(
            oid=oid,
            annotation=annotation,
            authlist=authlist,
        )
        end = time.perf_counter_ns() - start
        first_contact_execution_times.append(end)
        time.sleep(SLEEP_TIME)

    print(f"First contact: {first_contact_execution_times}")

    annotate_execution_times = []
    annotate_oids = []
    for authlist_size in range(1, NUMBER_OF_CLIENTS):
        print(f"Annotating {authlist_size} clients...")
        authlist = list(map(lambda c: c.id, clients[1 : 1 + authlist_size]))
        annotation = generate_random_string(1024)
        oid = generate_random_string(8)

        start = time.perf_counter_ns()
        clients[0].annotate(
            oid=oid,
            annotation=annotation,
            authlist=authlist,
        )
        end = time.perf_counter_ns() - start

        annotate_execution_times.append(end)
        annotate_oids.append(oid)
        time.sleep(SLEEP_TIME)

    print(f"Annotate times: {annotate_execution_times}")

    delete_execution_times = []
    for authlist_size in range(1, NUMBER_OF_CLIENTS):
        print(f"Deleting annotation from {authlist_size} clients...")
        authlist = list(map(lambda c: c.id, clients[1 : 1 + authlist_size]))
        oid = generate_random_string(8)

        start = time.perf_counter_ns()
        clients[0].delete_annotation(
            oid=oid,
            authlist=authlist,
        )
        end = time.perf_counter_ns() - start

        delete_execution_times.append(end)
        time.sleep(SLEEP_TIME)

    print(f"Delete times: {delete_execution_times}")

    get_execution_times = []
    get_oid = generate_random_string(8)
    for get_size in range(1, NUMBER_OF_CLIENTS):
        print(f"Setting up {get_size} annotations...")
        annotation = generate_random_string(1024)
        clients[get_size].annotate(
            oid=get_oid, annotation=annotation, authlist=[clients[0].id]
        )
        time.sleep(SLEEP_TIME)

        print(f"Getting {get_size} annotations...")
        start = time.perf_counter_ns()
        clients[0].get_annotation(
            oid=get_oid,
        )
        end = time.perf_counter_ns() - start

        print("TIME", end)

        get_execution_times.append(end)
        time.sleep(SLEEP_TIME)

    print(f"Get times: {get_execution_times}")

    return {
        "first_contact": first_contact_execution_times,
        "annotate": annotate_execution_times,
        "delete": delete_execution_times,
        "get": get_execution_times,
    }


proxies = {
    "stub": (
        StubTigroServerProxy(
            server_uri=TIGRO_SERVER_URI,
        ),
        True,
    ),
    "single_circuit": (
        SingleCircuitTigroServerProxy(
            server_uri=TIGRO_SERVER_URI,
            proxy_generator=TorProxyGenerator(tor_proxy_uri=TOR_PROXY_URI),
        ),
        True,
    ),
    "circuit_per_box": (
        CircuitPerBoxTigroServerProxy(
            server_uri=TIGRO_SERVER_URI,
            proxy_generator=TorProxyGenerator(tor_proxy_uri=TOR_PROXY_URI),
        ),
        False,
    ),
}

aggregated_results = {}
for name, tup in proxies.items():
    proxy, batch = tup
    print(f"benchmarking {name}...")
    aggregated_results[name] = benchmark(proxy, batch)

pprint.pprint(aggregated_results)

with open("results.txt", "w") as f:
    f.write(str(aggregated_results))
