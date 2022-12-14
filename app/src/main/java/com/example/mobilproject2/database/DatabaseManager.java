package com.example.mobilproject2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mobilproject2.database.model.Contact;
import com.example.mobilproject2.database.model.Subject;
import com.example.mobilproject2.database.table.ContactTable;
import com.example.mobilproject2.database.table.SubjectTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseManager extends SQLiteOpenHelper
{
    public static final int SELECTION_MINIMUM_MARK = 60;
    public static final String SELECTION_PROFESSOR = "Кондратюк Ю.В.";
    public static final String SELECTION_CONTACT_ENDING = "ович";
    private static final String QUERY_CREATE_CONTACT_TABLE =
            "CREATE TABLE " + ContactTable.TABLE_NAME + " (" +
                    ContactTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    ContactTable.COLUMN_NAME_FIRST_NAME + " TEXT," +
                    ContactTable.COLUMN_NAME_MIDDLE_NAME + " TEXT," +
                    ContactTable.COLUMN_NAME_LAST_NAME + " TEXT," +
                    ContactTable.COLUMN_NAME_NUMBER + " TEXT," +
                    ContactTable.COLUMN_NAME_ADDRESS + " TEXT)";
    private static final String QUERY_CREATE_STUDENT_TABLE =
            "CREATE TABLE " + SubjectTable.TABLE_NAME + " (" +
                    SubjectTable.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    SubjectTable.COLUMN_NAME + " STRING," +
                    SubjectTable.COLUMN_PROFESSOR + " STRING," +
                    SubjectTable.COLUMN_ADDRESS + " STRING," +
                    SubjectTable.COLUMN_MARK + " INTEGER)";

    public DatabaseManager(@Nullable Context context)
    {
        super(context, "wheat.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(QUERY_CREATE_STUDENT_TABLE);
        sqLiteDatabase.execSQL(QUERY_CREATE_CONTACT_TABLE);

        Subject[] subjects = {
                new Subject("Name1", "Professor1", "Address1", 1),
                new Subject("Name2", "Professor2", "Address2", 2),
                new Subject("Name3", "Professor3", "Address3", 3),
                new Subject("Name4", "Professor4", "Address4", 4),
        };

        for (Subject subject : subjects)
            this.insertSubject(subject, sqLiteDatabase);

        Contact[] contacts = {
                new Contact("Тарас", "Олександрович", "Мельник", "123-456", "вул. Хрещатик, 24"),
                new Contact("Віктор", "Семенович", "Шевченко", "789-012", "вул. Васильківська, 37"),
                new Contact("Олена", "Ігорівна", "Коваленко", "345-678", "вул. Богдана Хмельницького, 65"),
                new Contact("Богдан", "Сергійович",  "Бондаренко", "901-234", "вул. Голосіївська, 15"),
                new Contact("Володимир", "Максимович", "Бойко", "567-890", "вул. Тараса Шевченка, 149"),
        };

        for (Contact contact : contacts)
            insertContact(contact, sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

    public void insertSubject(Subject subject, SQLiteDatabase database)
    {
        ContentValues values = new ContentValues();

        values.put(SubjectTable.COLUMN_NAME, subject.getName());
        values.put(SubjectTable.COLUMN_PROFESSOR, subject.getProfessor());
        values.put(SubjectTable.COLUMN_ADDRESS, subject.getAddress());
        values.put(SubjectTable.COLUMN_MARK, subject.getMark());
        database.insert(SubjectTable.TABLE_NAME, null, values);
    }

    public void insertSubject(Subject subject)
    {
        try (SQLiteDatabase database = getWritableDatabase())
        {
            this.insertSubject(subject, database);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteSubject(int id)
    {
        try (SQLiteDatabase database = getWritableDatabase())
        {
            database.delete(SubjectTable.TABLE_NAME, SubjectTable.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<Subject> getAllSubjects()
    {
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s", SubjectTable.TABLE_NAME);

        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(query, null)
        )
        {
            ArrayList<Subject> subjects = new ArrayList<>();

            while (cursor.moveToNext())
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_NAME));
                String professor = cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_PROFESSOR));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_ADDRESS));
                int mark = cursor.getInt(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_MARK));

                Subject subject = new Subject(id, name, professor, address, mark);
                subjects.add(subject);
            }

            return subjects;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<Subject> getSelectionSubject()
    {
        String query = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE %s = '%s' and %s > '%d'", SubjectTable.TABLE_NAME, SubjectTable.COLUMN_PROFESSOR, SELECTION_PROFESSOR, SubjectTable.COLUMN_MARK, SELECTION_MINIMUM_MARK);

        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(query, null)
        )
        {
            ArrayList<Subject> subjects = new ArrayList<>();

            while (cursor.moveToNext())
            {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_NAME));
                String professor = cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_PROFESSOR));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_ADDRESS));
                int mark = cursor.getInt(cursor.getColumnIndexOrThrow(SubjectTable.COLUMN_MARK));

                Subject subject = new Subject(id, name, professor, address, mark);
                subjects.add(subject);
            }

            return subjects;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public int getSelectionSubjectValue()
    {
        String selectedValue = "selectedValue";
        String query = String.format(
                Locale.getDefault(), "SELECT SUM(%s) / COUNT(%s) AS %s FROM %s", SubjectTable.COLUMN_MARK, SubjectTable.COLUMN_MARK, selectedValue, SubjectTable.TABLE_NAME
        );

        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(query, null)
        )
        {
            if (cursor.moveToNext())
                return cursor.getInt(cursor.getColumnIndexOrThrow(selectedValue));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public void insertContact(Contact contact, SQLiteDatabase database)
    {
        ContentValues values = new ContentValues();

        values.put(ContactTable.COLUMN_NAME_FIRST_NAME, contact.getFirstName());
        values.put(ContactTable.COLUMN_NAME_MIDDLE_NAME, contact.getMiddleName());
        values.put(ContactTable.COLUMN_NAME_LAST_NAME, contact.getLastName());
        values.put(ContactTable.COLUMN_NAME_NUMBER, contact.getPhoneNumber());
        values.put(ContactTable.COLUMN_NAME_ADDRESS, contact.getAddress());
        database.insert(ContactTable.TABLE_NAME, null, values);
    }

    public List<Contact> getAllContacts()
    {
        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM " + ContactTable.TABLE_NAME, null)
        )
        {
            ArrayList<Contact> contacts = new ArrayList<>();

            while (cursor.moveToNext())
            {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_FIRST_NAME));
                String middleName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_MIDDLE_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_LAST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_ADDRESS));
                Contact contact = new Contact(firstName, middleName, lastName, phoneNumber, address);

                contacts.add(contact);
            }

            return contacts;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<Contact> getContactsWithName()
    {
        try (
                SQLiteDatabase database = getReadableDatabase();
                Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s WHERE %s LIKE '%%%s'", ContactTable.TABLE_NAME, ContactTable.COLUMN_NAME_MIDDLE_NAME, DatabaseManager.SELECTION_CONTACT_ENDING), null)
        )
        {
            ArrayList<Contact> contacts = new ArrayList<>();

            while (cursor.moveToNext())
            {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_FIRST_NAME));
                String middleName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_MIDDLE_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_LAST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_NUMBER));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(ContactTable.COLUMN_NAME_ADDRESS));
                Contact contact = new Contact(firstName, middleName, lastName, phoneNumber, address);

                contacts.add(contact);
            }

            return contacts;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
