package aero.panasonic.sample.permission.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SecretDataProvider extends ContentProvider {

//    private static final String TAG = SecretDataProvider.class.getSimpleName();

    static final String PROVIDER_NAME = "aero.panasonic.sample.provider";
    static final String URL = "content://" + PROVIDER_NAME + "/data";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String KEY_SECRET = "secret";
    static final String KEY_PUBLIC = "public";
    static final int SECRET = 1;
    static final int PUBLIC = 2;
    static final UriMatcher uriMatcher;
    private static final File ROOT_PATH = Environment.getExternalStorageDirectory();

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "data/secret", SECRET);
        uriMatcher.addURI(PROVIDER_NAME, "data/public", PUBLIC);
    }

    File FILE_PATH = new File(ROOT_PATH, Environment.DIRECTORY_DOWNLOADS);
    File FILE = new File(FILE_PATH, "property.txt");
    String[] columns = new String[]{"_id", "name", "value"};

    public SecretDataProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SECRET:
                return "vnd.android.cursor.item/vnd.aero.panasonic.sample.data.item";
            case PUBLIC:
                return "vnd.android.cursor.item/vnd.aero.panasonic.sample.data.item";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case SECRET:
                return getProperty(KEY_SECRET);

            case PUBLIC:
                return getProperty(KEY_PUBLIC);

            default:
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return update(uri, values);
    }

    private int update(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {

            case PUBLIC:
                setProperty(KEY_PUBLIC, values.getAsString(KEY_PUBLIC));
                break;

            case SECRET:
                setProperty(KEY_SECRET, values.getAsString(KEY_SECRET));
                break;

            default:
                break;
        }
        return 1;
    }

    private void setProperty(String key, String value) {
        try {
            if (!FILE.exists())
                FILE.createNewFile();
            InputStream inputStream = new FileInputStream(FILE);
            Properties properties = new Properties();
            properties.load(inputStream);
            OutputStream outputStream = new FileOutputStream(FILE);
            properties.setProperty(key, value);
            properties.store(outputStream, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MatrixCursor getProperty(String key) {
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        try {
            if (!FILE.exists())
                FILE.createNewFile();
            InputStream inputStream = new FileInputStream(FILE);
            Properties properties = new Properties();
            properties.load(inputStream);
            String value = properties.getProperty(key);
            if (value != null && !value.equals("")) {
                matrixCursor.addRow(new Object[]{1, key, value});
            } else {
                matrixCursor.addRow(new Object[]{1, key, "default-value"});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrixCursor;
    }
}
