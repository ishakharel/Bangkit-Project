from tensorflow import keras
from tensorflow.keras.preprocessing import image
from flask import Flask, request, jsonify
from PIL import Image
import numpy as np

app = Flask(__name__)

model = keras.models.load_model("model.h5")


@app.route("/upload", methods=["POST"])
def upload():
    if "image" not in request.files:
        return jsonify({"error": "No image uploaded"})

    image_file = request.files["image"]

    try:
        img = image.load_img(image_file.filename, target_size=(200, 200))
        x = image.img_to_array(img)
        x = x / 255.0
        x = np.expand_dims(x, axis=0)
        images = np.vstack([x])
        classes = model.predict(images, batch_size=20)
        print("Predicted class:", classes[0])
        # Add the following line to print the perspective class
        perspective_class = np.argmax(classes[0])
        print("Perspective class:", perspective_class)

        return jsonify({"predicted_class": str(perspective_class)})

    except Exception as e:
        return jsonify({"error": str(e)})


if __name__ == "__main__":
    app.run()
