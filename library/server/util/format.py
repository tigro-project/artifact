##
## Copyright 2025 The Tigro Authors.
##


def format_redis_key(po_id: str, label: str) -> str:
    return f"{po_id}|{label}"
