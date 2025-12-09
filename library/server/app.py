##
## Copyright 2025 The Tigro Authors.
##

from flask import Flask, jsonify, request
from util.format import format_redis_key
from waitress import serve

import redis
import uuid

app = Flask(__name__)

r = redis.Redis(host="localhost", port=6379, db=0, decode_responses=True)


@app.route("/hello", methods=["GET"])
def hello():
    return jsonify(response="hello")


@app.route("/create_box", methods=["POST"])
def create_box():
    return jsonify(box_id=uuid.uuid4())


@app.route("/drop_mails", methods=["POST"])
def drop_mails():
    po_ids = request.form.getlist("po_id[]")
    labels = request.form.getlist("label[]")
    mails = request.form.getlist("mail[]")

    success = True
    for po_id, label, mail in zip(po_ids, labels, mails):
        success = success and r.set(format_redis_key(po_id, label), mail)
    return jsonify(success=success)


@app.route("/drop_mail/<po_id>/<label>", methods=["POST"])
def drop_mail(po_id, label):
    data = request.form.get("mail")

    return jsonify(success=r.set(format_redis_key(po_id, label), data))


@app.route("/get_mails", methods=["GET"])
def get_mails():
    po_ids = request.form.getlist("po_id[]")
    labels = request.form.getlist("label[]")

    ciphertexts = [
        r.get(format_redis_key(po_id, label)) for po_id, label in zip(po_ids, labels)
    ]
    return jsonify(mails=ciphertexts)


@app.route("/get_mail/<po_id>/<label>", methods=["GET"])
def get_mail(po_id, label):
    return jsonify(mail=r.get(format_redis_key(po_id, label)))


@app.route("/delete_mails", methods=["DELETE"])
def delete_mails():
    po_ids = request.form.getlist("po_id[]")
    labels = request.form.getlist("label[]")

    for po_id, label in zip(po_ids, labels):
        r.delete(format_redis_key(po_id, label))

    return jsonify(success=True)


@app.route("/delete_mail/<po_id>/<label>", methods=["DELETE"])
def delete_mail(po_id, label):
    r.delete(format_redis_key(po_id, label))
    return jsonify(success=True)


if __name__ == "__main__":
    serve(app, host="0.0.0.0", port=5000, channel_timeout=2, cleanup_interval=2)
