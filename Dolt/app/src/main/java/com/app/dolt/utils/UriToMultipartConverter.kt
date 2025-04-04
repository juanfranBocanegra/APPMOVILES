import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Objeto que proporciona una utilidad para convertir un [Uri] en un [MultipartBody.Part],
 * lo que permite enviar archivos a través de solicitudes HTTP con Retrofit.
 */
object UriToMultipartConverter {

    /**
     * Convierte un [Uri] en un [MultipartBody.Part] listo para enviar con Retrofit.
     *
     * @param uri El [Uri] del archivo que se desea convertir (por ejemplo, una imagen desde la galería o el sistema de archivos).
     * @param context El contexto de Android necesario para acceder al ContentResolver.
     * @param formDataName El nombre del campo en el formulario HTTP en el que se enviará el archivo (por defecto, "file").
     * @return Un objeto [MultipartBody.Part] listo para enviar en una solicitud o `null` si ocurre un error en la conversión.
     */
    fun convert(
        uri: Uri,
        context: Context,
        formDataName: String = "file"
    ): MultipartBody.Part? {
        return try {
            // Obtener el tipo MIME del archivo (por ejemplo, "image/png").
            val mimeType = context.contentResolver.getType(uri) ?: "image/*"

            // Obtener la extensión adecuada a partir del tipo MIME.
            val fileExtension = mimeType.split("/").lastOrNull() ?: "jpg"

            // Crear un archivo temporal en el almacenamiento caché de la aplicación.
            val file = createTempFile(context, uri, "img_", ".$fileExtension")

            // Crear un MultipartBody.Part para enviar el archivo en una solicitud HTTP.
            MultipartBody.Part.createFormData(
                formDataName,
                file.name,
                file.asRequestBody(mimeType.toMediaTypeOrNull())
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Crea un archivo temporal en el directorio de caché de la aplicación a partir de un [Uri].
     *
     * @param context El contexto de la aplicación necesario para acceder al ContentResolver.
     * @param uri El [Uri] del archivo de origen.
     * @param prefix Prefijo del nombre del archivo temporal.
     * @param suffix Sufijo del nombre del archivo temporal (por ejemplo, ".jpg").
     * @return Un [File] temporal con el contenido del archivo original.
     */
    private fun createTempFile(
        context: Context,
        uri: Uri,
        prefix: String,
        suffix: String
    ): File {
        // Crear un archivo temporal en el almacenamiento de caché de la aplicación.
        val tempFile = File.createTempFile(prefix, suffix, context.cacheDir)

        // Copiar el contenido del archivo original en el nuevo archivo temporal.
        context.contentResolver.openInputStream(uri)?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }
}
