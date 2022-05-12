package mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.ui.gallery

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import mx.tecnm.tepic.ladm_u3_practica2_firebaseveterinaria_saucedogarciajuanluis.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val baseRemota= FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnBuscarPropietario.setOnClickListener {
            var select =baseRemota.collection("propietario")
            if (binding.etNombre.text.toString() == "") {
                if (binding.etTelefono.text.toString() == "") {
                    if (binding.etEdadM.text.toString() == "") {
                        Toast.makeText(
                            requireContext(),
                            "Ingrese un parametro de busqueda",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }else{
                        var select=baseRemota.collection("propietario").whereLessThanOrEqualTo("edad",binding.etEdadMa.text.toString().toInt())
                        mostrarListaEdad(select)
                    }
                }else{
                    var select=baseRemota.collection("propietario").whereEqualTo("telefono",binding.etTelefono.text.toString())
                    mostrarLista(select)
                }
            }else{
                var select=baseRemota.collection("propietario").whereEqualTo("nombre",binding.etNombre.text.toString())
                mostrarLista(select)
            }
        }
        return root
    }

    fun mostrarListaEdad(qur :Query) {
        qur.addSnapshotListener { query, error ->
                if (error != null) {
                    //ERROR
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }
                val llenar = ArrayList<String>()
                for (documento in query!!) {
                    if(binding.etEdadM.text.toString().toInt()<=documento.getLong("edad").toString().toInt()) {
                        var cadena =
                            "${documento.getString("curp")} ${documento.getString("nombre")}  ${
                                documento.getLong("edad")
                            } años"
                        llenar.add(cadena)
                    }
                }
                binding.lista.adapter =
                    ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, llenar)
            }
    }

    fun mostrarLista(qur :Query) {
        qur.addSnapshotListener { query, error ->
            if (error != null) {
                //ERROR
                AlertDialog.Builder(requireContext())
                    .setMessage(error.message)
                    .show()
                return@addSnapshotListener
            }
            val llenar = ArrayList<String>()
            for (documento in query!!) {
                    var cadena =
                        "${documento.getString("curp")} ${documento.getString("nombre")}  ${
                            documento.getLong("edad")
                        } años"
                    llenar.add(cadena)
            }
            binding.lista.adapter =
                ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, llenar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}