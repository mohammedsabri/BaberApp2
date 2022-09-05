package com.example.barberapp.ui.appointments

import android.app.AlertDialog
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
import com.example.barberapp.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class AppointmentFragment : Fragment(), BookClickListener {

    lateinit var app: BookXApp
    lateinit var loader : AlertDialog
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
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
//          fragBinding.fab.setOnClickListener{
//              val action =AppointmentFragmentDirections.actionNavAppointmentsToNavBook()
//              findNavController().navigate(action)
//          }
////
//        }
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


          showLoader(loader, "Downloading Books")
        appointmentViewModel.observableBooksList.observe(viewLifecycleOwner, Observer { books ->
            books?.let {
                render(books as ArrayList<BookModel>)
                  hideLoader(loader)
                  checkSwipeRefresh()
            }
        })

          setSwipeRefresh()

        /* This is a swipe to delete handler. */
        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as BookAdapter
                showLoader(loader, "Deleting Book")




                appointmentViewModel.delete(
                    appointmentViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as BookModel).uid!!
                )
                adapter.removeAt(viewHolder.bindingAdapterPosition)
                  hideLoader(loader)
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

    /**
     * We are inflating the menu, finding the toggle button, setting the checked state, and setting the
     * onCheckedChangeListener
     *
     * @param menu The menu object that you want to inflate.
     * @param inflater The MenuInflater that you use to inflate your menu resource into the Menu
     * object.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appointment, menu)


        val item = menu.findItem(R.id.toggleBooks) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleBooks: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
        toggleBooks.isChecked = false

        toggleBooks.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) appointmentViewModel.loadAll()
            else appointmentViewModel.load()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }










    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun render(booksList: ArrayList<BookModel>) {
        fragBinding.recyclerView.adapter = BookAdapter(booksList,this,appointmentViewModel.readOnly.value!!)//,this)
        if (booksList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
           // fragBinding.booksNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
           // fragBinding.booksNotFound.visibility = View.GONE
        }
        appointmentViewModel.readOnly.value!!
    }
//        override fun onBookClick(book: BookModel) {
//        val action = AppointmentFragmentDirections.actionAppointmentFragmentToBookDetailFragment(book.id)
//          findNavController().navigate(action)
//     }

    /**
     * > When a book is clicked, navigate to the book detail fragment
     *
     * @param book BookModel - This is the book that was clicked.
     */

    override fun onBookClick(book: BookModel) {
//        val action = AppointmentFragmentDirections.actionAppointmentFragmentToBookDetailFragment(book.uid!!)
        val action =AppointmentFragmentDirections.actionNavAppointmentsToNavBook(book.uid!!)
        println("HERE")
        println(book)
       if(!appointmentViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }
    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader, "Downloading Donations")
            if(appointmentViewModel.readOnly.value!!)
                appointmentViewModel.loadAll()
            else
                appointmentViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
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