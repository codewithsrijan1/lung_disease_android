package com.example.lungcancerdetector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class LungCancerClassifier(private val context: Context) {

    private val labels = listOf(
        "Adenocarcinoma",
        "Large Cell",
        "Normal",
        "Squamous"
    )

    private var interpreter: Interpreter? = null

    init {
        try {
            val assetFileDescriptor = context.assets.openFd("lung_cancer_xception.tflite")
            val inputStream = assetFileDescriptor.createInputStream()
            val fileChannel = inputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val buffer = fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            interpreter = Interpreter(buffer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun classifyFromUri(uri: Uri): List<Pair<String, Float>> {
        return try {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.isMutableRequired = true
            }
            classify(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            labels.map { it to 0.0f }
        }
    }

    fun classify(bitmap: Bitmap): List<Pair<String, Float>> {
        val currentInterpreter = interpreter ?: return labels.map { it to 0.0f }

        val resized = Bitmap.createScaledBitmap(bitmap, 350, 350, true)
        val input = ByteBuffer.allocateDirect(4 * 350 * 350 * 3)
        input.order(ByteOrder.nativeOrder())

        val intValues = IntArray(350 * 350)
        resized.getPixels(intValues, 0, 350, 0, 0, 350, 350)

        for (pixelValue in intValues) {
            input.putFloat(((pixelValue shr 16) and 0xFF) / 255f)
            input.putFloat(((pixelValue shr 8) and 0xFF) / 255f)
            input.putFloat((pixelValue and 0xFF) / 255f)
        }

        val output = Array(1) { FloatArray(4) }
        currentInterpreter.run(input, output)

        return labels.zip(output[0].toList())
            .sortedByDescending { it.second }
    }
}
