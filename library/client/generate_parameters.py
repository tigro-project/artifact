##
## Copyright 2025 The Tigro Authors.
##

from cryptography.hazmat.primitives.serialization import (
    Encoding,
    ParameterFormat,
)
from cryptography.hazmat.primitives.asymmetric import dh

DH_GENERATOR = 2
DH_KEY_SIZE = 2048

with open("dh-parameters.pem", "wb") as output:
    dh_parameters = dh.generate_parameters(generator=DH_GENERATOR, key_size=DH_KEY_SIZE)
    param_bytes = dh_parameters.parameter_bytes(Encoding.PEM, ParameterFormat.PKCS3)
    output.write(param_bytes)
