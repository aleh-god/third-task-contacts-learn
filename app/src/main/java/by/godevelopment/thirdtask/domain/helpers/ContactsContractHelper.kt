package by.godevelopment.thirdtask.domain.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
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
                key = it.toString(),
                name = "name $it",
                taskPhoneNumber = "Number $it",
                email = "email $it",
                surname = "surname $it",
                isSelected = false
            )
        }
        emit(list)
    }

    @SuppressLint("Range")
    fun getContactList(): Flow<List<ContactModel>> = flow {
        val idList = mutableListOf<String>()
        val keyList = mutableListOf<String>()
        val names = mutableListOf<String>()
        val surnames = mutableListOf<String>()
        val numbers = mutableListOf<String>()
        val emails = mutableListOf<String>()

        val uri: Uri = ContactsContract.Contacts.CONTENT_URI
        val sortQuery= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        val cursorNameOrNull = context.contentResolver.query(
            uri,
            null,
            null,
            null,
            sortQuery
        )

        cursorNameOrNull?.let { cursorName ->

            if(cursorName.count > 0) {
                while (cursorName.moveToNext()) {
                    val id = cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts._ID))
                    idList.add(id)
                    Log.i(TAG, "ContactsContractHelper: id = $id")
                    val key = cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                    keyList.add(key)
                    Log.i(TAG, "ContactsContractHelper: key = $key")
                    val name = cursorName.getString(cursorName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    names.add(name)
                    Log.i(TAG, "ContactsContractHelper: name = $name")

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
                                Log.i(TAG, "ContactsContractHelper: email = $email")
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
                                Log.i(TAG, "ContactsContractHelper: number = $number")
                            }
                        }
                        cursorPhone.close()
                    }

                    val uriFamilyName = ContactsContract.Data.CONTENT_URI
                    val selectionFamilyNameQuery = ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " =?"
                    val cursorFamilyNameOrNull = context.contentResolver.query(
                        uriFamilyName,
                        null,
                        selectionFamilyNameQuery,
                        arrayOf<String>(id),
                        null
                    )
                    cursorFamilyNameOrNull?.let { cursorFamilyName ->
                        Log.i(TAG, "getContactList: cursorFamilyName = ${cursorFamilyName.count}")
//                        if (cursorFamilyName.count > 0) {
//                            while (cursorFamilyName.moveToNext()) {
//                                val familyName = cursorFamilyName.getString(cursorFamilyName.getColumnIndex(
//                                    ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME))
//                                surnames.add(familyName)
//                                Log.i(TAG, "ContactsContractHelper: familyName = $familyName")
//                            }
//                        }
                        cursorFamilyName.close()
                    }
                }
            }
            cursorName.close()
        }
        val contacts = names.mapIndexed { index, name ->
            Log.i(TAG, "names.mapIndexed: index = $index key = ${keyList[index]} name = $name surname = ${surnames[index]} number = ${numbers[index]}")
            ContactModel(
                id = idList[index].toInt(),
                key = keyList[index],
                name = name,
                taskPhoneNumber = numbers[index],
                email = emails[index],
                surname = surnames[index],
                isSelected = false
            )
        }
        Log.i(TAG, "getContactList: contacts = ${contacts.size}")
        emit(contacts)
    }
}