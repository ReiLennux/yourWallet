import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplelogin.data.model.Card
import com.example.simplelogin.viewmodel.AuthViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(private val authViewModel: AuthViewModel) : ViewModel() {
    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards

    init {
        fetchCards()
    }

    fun fetchCards() {
        viewModelScope.launch {
            val userId = authViewModel.getUserId()
            if (!userId.isNullOrEmpty()) {
                val database = FirebaseDatabase.getInstance().reference
                fetchCards(userId, database) { fetchedCards ->
                    _cards.value = fetchedCards
                }
            }
        }
    }

    private fun fetchCards(
        userId: String,
        database: DatabaseReference,
        onCardsFetched: (List<Card>) -> Unit
    ) {
        val userRef = database.child("users").child(userId).child("cards")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cardList = mutableListOf<Card>()
                for (snapshot in dataSnapshot.children) {
                    val card = snapshot.getValue(Card::class.java)
                    card?.let { cardList.add(it) }
                }
                onCardsFetched(cardList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}
