package com.example.notestar.repository

import com.example.notestar.Constants
import com.example.notestar.models.Notes
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class StorageRepository() {
    val currentUser = Firebase.auth.currentUser
    val hasUser = Firebase.auth.currentUser != null

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()
    private val notesRef: CollectionReference =
        Firebase.firestore.collection(Constants.NOTES_COLLECTION_REF)

    fun getUserNotes(
        userid: String
    ): Flow<Resources<List<Notes>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = notesRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userid)
                .addSnapshotListener { snapshot, error ->
                    val response = if (snapshot != null) {
                        val notes = snapshot.toObjects(Notes::class.java)
                        Resources.Success(data = notes)
                    } else {
                        Resources.Error(throwable = error?.cause)
                    }
                    trySend(response)
                }

        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }

    }

    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit
    ) {
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(Notes::class.java))
            }
            .addOnFailureListener {
                onError.invoke(it.cause)
            }
    }

    fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        color: Int = 0,
        onComplete: (Boolean) -> Unit,
    ) {
        val documentId = notesRef.document().id
        val note = Notes(
            userId,
            title,
            description,
            timestamp,
            colorIndex = color,
            documentId = documentId
        )
        notesRef
            .document(documentId)
            .set(note)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    fun deleteNote(
        noteId: String,
        onComplete: (Boolean) -> Unit
    ) {
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updateNote(
        title: String,
        note: String,
        color: Int,
        noteId: String,
        onResult: (Boolean) -> Unit
    ) {
        val updateData = hashMapOf<String, Any>(
            "colorIndex" to color,
            "description" to note,
            "title" to title,
        )

        notesRef.document(noteId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }

    fun signOut() = Firebase.auth.signOut()

}