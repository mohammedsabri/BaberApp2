package com.example.barberapp.ui.appointments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barberapp.R
import com.example.barberapp.adapters.BookAdapter
import com.example.barberapp.adapters.BookClickListener
import com.example.barberapp.auth.LoggedInViewModel
import com.example.barberapp.databinding.FragmentAppointmentsBinding
import com.example.barberapp.main.BookXApp
import com.example.barberapp.models.BookModel
import com.example.barberapp.utils.SwipeToDeleteCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import  com.example.barberapp.utils.SwipeToEditCallback
import java.util.*


class AppointmentFragment : Fragment(), BookClickListener {

    lateinit var app: BookXApp
    private var _fragBinding: FragmentAppointmentsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var appointmentViewModel: AppointmentViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
//
//        fragBinding.fab.setOnClickListener {
//            val action = AppointmentFragmentDirections.actionAppointmentFragmentToBookFragment()
//            findNavController().navigate(action)
//        }
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        appointmentViewModel = ViewModelProvider(this).get(AppointmentViewModel::class.java)
        appointmentViewModel.observableBooksList.observe(viewLifecycleOwner, Observer {
                books ->
            books?.let {render(books as ArrayList<BookModel>)}
        })

//           val fab: FloatingActionButton = fragBinding.fab
//             fab.setOnClickListener {
//            val action = AppointmentFragmentDirections.actionAppointmentFragmentToBookFragment()
//            findNavController().navigate(action)
//        }
//        showLoader(loader, "Downloading Donations")
        appointmentViewModel.observableBooksList.observe(viewLifecycleOwner, Observer { books ->
            books?.let {
                render(books as ArrayList<BookModel>)
//                hideLoader(loader)
//                checkSwipeRefresh()
            }
        })

//        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as BookAdapter

                adapter.removeAt(viewHolder.bindingAdapterPosition)


                appointmentViewModel.delete(
                    appointmentViewModel.user?.uid!!,
                    (viewHolder.itemView.tag as BookModel).uid!!
                )
//                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

           val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
              override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                  onBookClick(viewHolder.itemView.tag as BookModel)
              }
            }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
          itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)
         return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appointment, menu)


        val item = menu.findItem(R.id.toggleBooks) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleBooks: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
        toggleBooks.isChecked = false

        toggleBooks.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) appointmentViewModel.loadAll()
            else appointmentViewModel.load()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }










    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(booksList: ArrayList<BookModel>) {
        fragBinding.recyclerView.adapter = BookAdapter(booksList)//,this)
        if (booksList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
           // fragBinding.booksNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
           // fragBinding.booksNotFound.visibility = View.GONE
        }
    }
//        override fun onBookClick(book: BookModel) {
//        val action = AppointmentFragmentDirections.actionAppointmentFragmentToBookDetailFragment(book.id)
//          findNavController().navigate(action)
//     }

    override fun onBookClick(book: BookModel) {
//        val action = AppointmentFragmentDirections.actionAppointmentFragmentToBookDetailFragment(book.uid!!)
        val action =AppointmentFragmentDirections.actionNavAppointmentsToNavBook(book.uid!!)
        if(!appointmentViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }


    override fun onResume() {
        super.onResume()
//        // for enabling night mode
//        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//// for disabling night mode
//        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//// for enabling night mode in auto mode
//        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
//// for enabling night mode while following system settings
//        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_FOLLOW_SYSTEM);
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                appointmentViewModel.liveFirebaseUser.value = firebaseUser
                appointmentViewModel.load()
            }
        })
        appointmentViewModel.load()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }


}