import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.sis.R
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Button

fun exceptionsAlert(context: Context) {
    Handler(Looper.getMainLooper()).post {
        // Usar un inflador explícito
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_permission_denied, null)

        // Crear el diálogo
        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Agregar animación de entrada

        // Cambiar el texto del mensaje
        val textView = dialogView.findViewById<TextView>(R.id.permission_message)
        textView.text = "No cuentas con permisos para acceder a este recurso"

        // Configurar el botón
        val closeButton = dialogView.findViewById<Button>(R.id.close_button)
        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // Mostrar el diálogo
        alertDialog.show()
    }
}

