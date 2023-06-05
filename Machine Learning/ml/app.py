from tensorflow import keras
from tensorflow.keras.preprocessing import image
from flask import Flask, request, jsonify, make_response
from PIL import Image
from google.cloud import storage
import numpy as np
import h5py
import gcsfs
import os


app = Flask(__name__)


def load_model_from_storage():
    storage_client = storage.Client()

    bucket_name = "ecoloops_bucket"
    model_folder = "ml-model"
    model_filname = "model.h5"

    local_model_path = "/model.h5"
    bucket = storage_client.get_bucket(bucket_name)
    blob = bucket.blob(os.path.join(model_folder, model_filname))
    blob.download_to_filename(local_model_path)

    loaded_model = keras.models.load_model(local_model_path)

    return loaded_model


# PROJECT_NAME = "ecoloopse-bangkit-project"
# CREDENTIALS = "key.json"
# MODEL_PATH = "gs://ecoloops_bucket/ml-model/model.h5"
# FS = gcsfs.GCSFileSystem(project=PROJECT_NAME, token=CREDENTIALS)


@app.route("/upload", methods=["POST"])
def upload():
    model = load_model_from_storage()

    # with FS.open(MODEL_PATH, "rb") as model_file:
    #     model_gcs = h5py.File(model_file, "r")
    #     model = keras.models.load_model(model_gcs)

    apikey = request.headers.get("apikey")

    if "image" not in request.files or "apikey" not in request.headers:
        return jsonify({"error": "Missing required request"})

    if apikey != "B1sM1Llaht0pi5C4p5t0N3":
        return make_response(
            jsonify({"status": "error", "message": "API key is invalid"}), 401
        )

    image_file = request.files["image"]
    image_file.save("img.jpg")
    path = "img.jpg"

    try:
        img = image.load_img(path, target_size=(200, 200))
        x = image.img_to_array(img)
        x = x / 255.0
        x = np.expand_dims(x, axis=0)
        images = np.vstack([x])
        classes = model.predict(images, batch_size=20)
        print("Predicted class:", classes[0])
        # Add the following line to print the perspective class
        perspective_class = np.argmax(classes[0])
        print("Perspective class:", perspective_class)

        # os.remove(path)
        return jsonify({"predicted_class": str(perspective_class)})

    except Exception as e:
        return jsonify({"error": str(e)})


if __name__ == "__main__":
    app.run()
