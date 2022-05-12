package mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.ui.slideshow

import android.R
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.internal.JsonUtil.getString
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.FragmentSlideshowBinding
import java.lang.NullPointerException

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var listaID = ArrayList<String>()
    val baseRemota= FirebaseFirestore.getInstance()
    var idGlobal=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        FirebaseFirestore.getInstance().collection("propietario")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //ERROR
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                listaID.clear()
                val llenar = ArrayList<String>()
                for (documento in query!!) {
                    var cadena =
                        "${documento.getString("curp")} ${documento.getString("nombre")}  ${
                            documento.getLong("edad")
                        } años"
                    llenar.add(cadena)
                    listaID.add(documento.id)
                }
                try {
                    binding.lista.adapter =ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, llenar)
                    binding.lista.setOnItemClickListener { adapterView, view, pos, l ->
                        val idEl = listaID.get(pos)
                        idGlobal = idEl

                        AlertDialog.Builder(requireContext())
                            .setTitle("Atención")
                            .setMessage("¿Qué desea hacer?")
                            .setNeutralButton("Eliminar") { d, i ->
                                eliminar(idEl)
                            }
                            .setNegativeButton("Actualizar") { d, i ->
                                actualizar(idEl)
                            }
                            .show()
                    }
                } catch (err: NullPointerException) {
                }
            }

       binding.btnActualizar.setOnClickListener {
          try {
              val baseRemota=FirebaseFirestore.getInstance()
              baseRemota.collection("propietario")
                  .document(idGlobal)
                  .update("nombre" , binding.etNombre.text.toString(),
                      "telefono" , binding.etTelefono.text.toString(),
                      "edad" ,  binding.etEdad.text.toString().toInt())
                  .addOnSuccessListener {
                      Toast.makeText(requireContext(), "Se actualizó con exito", Toast.LENGTH_LONG)
                          .show()
                      binding.etNombre.setText("")
                      binding.etTelefono.setText("")
                      binding.etEdad.setText("")
                  }
                  .addOnFailureListener {
                      AlertDialog.Builder(requireContext())
                          .setMessage(it.message)
                          .show()
                  }
          }catch (err: NullPointerException){}
        }


        return root
    }

    private fun actualizar(idEl: String) {
        baseRemota.collection("propietario").document(idEl).get()
            .addOnSuccessListener {
                binding.etNombre.setText(it.getString("nombre"))
                binding.etTelefono.setText(it.getString("telefono"))
                binding.etEdad.setText(it.getLong("edad").toString())
            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(it.message)
                    .show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun eliminar(id: String) {
        try {
            val baseRemota= FirebaseFirestore.getInstance()
            baseRemota.collection("propietario")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Se eliminó el propietario", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
        }catch (err:NullPointerException){}
    }
}