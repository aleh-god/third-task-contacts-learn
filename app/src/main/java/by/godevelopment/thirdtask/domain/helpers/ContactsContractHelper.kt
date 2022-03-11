package by.godevelopment.thirdtask.domain.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import by.godevelopment.thirdtask.common.TAG
import by.godevelopment.thirdtask.domain.models.ContactModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactsContractHelper @Inject constructor(
    private val context: Context
) {
    init {
        Log.i(TAG, "ContactsContractHelper: init")
    }

    fun getTestList(): Flow<List<ContactModel>> = flow {
        val list = (0..10).map {
            ContactModel(
                id = it,
                name = "name $it",
                taskPhoneNumber = "Numbe $it",
                email = "email $it",
                surname = "surname $it",
                isSelected = false
            )
        }
        emit(list)
    }

    @SuppressLint("Range")
    fun getContactList(): Flow<List<ContactModel>> = flow {
        Log.i(TAG, "ContactsContractHelper: getContactList")
        val names = mutableListOf<String>()
        val numbers = mutableListOf<String>()
        val emails = mutableListOf<String>()
        // init uri
        val uri: Uri = ContactsContract.Contacts.CONTENT_URI
        // Sort
        val sortQuery= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        // init cursor
        val cursorNameOrNull = context.contentResolver.query(uri, null, null, null, sortQuery)
        // check condition
        cursorNameOrNull?.let { cursorName ->
            Log.i(TAG, "ContactsContractHelper: cursorName = ${cursorName.count}")
            if(cursorName.count > 0) {
                while (cursorName.moveToNext()) {
                    val id = cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts._ID))

                    val name = cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    names.add(name)
                    Log.i(TAG, "getContactList: name = $name")

                    val uriEmail =  ContactsContract.CommonDataKinds.Email.CONTENT_URI
                    val selectionEmailQuery = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =?"
                    val cursorEmailOrNull = context.contentResolver.query(
                        uriEmail,
                        null,
                        selectionEmailQuery,
                        arrayOf<String>(id),
                        null
                    )
                    cursorEmailOrNull?.let { cursorEmail ->
                        if (cursorEmail.count > 0) {
                            while (cursorEmail.moveToNext()) {
                                val email = cursorEmail.getString(cursorEmail.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Email.DATA
                                ))
                                emails.add(email)
                                Log.i(TAG, "getContactList: email = $email")
                            }
                        }
                        cursorEmail.close()
                    }

                    val uriPhone =  ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val selectionPhoneQuery = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                    val cursorPhoneOrNull = context.contentResolver.query(
                        uriPhone,
                        null,
                        selectionPhoneQuery,
                        arrayOf<String>(id),
                        null
                    )
                    cursorPhoneOrNull?.let { cursorPhone ->
                        if (cursorPhone.count > 0) {
                            while (cursorPhone.moveToNext()) {
                                val number = cursorPhone.getString(cursorPhone.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER))
                                numbers.add(number)
                                Log.i(TAG, "getContactList: number = $number")
                            }
                        }
                        cursorPhone.close()
                    }
                }
            }
            cursorName.close()
        }
        val contacts = names.mapIndexed { index, name ->
            ContactModel(
                id = 0,
                name = name,
                taskPhoneNumber = numbers[index],
                email = emails[index],
                surname = "",
                isSelected = false
            )
        }
        emit(contacts)
    }
}