package ai.free.vpn.tweeqoldvpn.getvpn.database;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.walhalla.ui.DLog;
import com.walhalla.vpnapp.model.Server;
import com.walhalla.vpnapp.repository.ServerRepository;

import java.util.ArrayList;
import java.util.List;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBHelper implements ServerRepository {

    private static final String TABLE_SERVERS = "servers";
    private static final String TABLE_BOOKMARK_SERVERS = "bookmark_servers";
    private static final String TAG = "@@@";
    private static DBHelper aa;

    public DatabaseReference databaseRef() {
        if (var0 == null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            var0 = firebaseDatabase.getReference();
        }
        return var0;
    }

    private DatabaseReference var0;

    public DBHelper() {
        // Initialize Firebase Realtime Database reference
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            var0 = firebaseDatabase.getReference();
        } catch (Exception e) {

        }
    }

    public static DBHelper newInstance(Context applicationContext) {
        if (aa == null) {
            aa = new DBHelper();
        }
        return aa;
    }

    @Override
    public void setInactive(Server server) {

        //none

//        DatabaseReference serverRef = databaseRef().child(TABLE_SERVERS).child(String.valueOf(server.id));
//        serverRef.child("quality").setValue(0);

    }

    @Override
    public Server getSimilarServer(String countryLong, String ip) {
        return null;
    }

    @Override
    public Server getGoodRandomServer(String selectedCountry) {
        return null;
    }

    @Override
    public void update(List<Server> serverList) {

    }

    public void clearTable() {

        //none

//        DatabaseReference serverRef = databaseRef().child(TABLE_SERVERS);
//        serverRef.removeValue();
    }

    public void setBookmark(Server server) {
        DatabaseReference bookmarkRef = databaseRef().child(TABLE_BOOKMARK_SERVERS).push();
        bookmarkRef.setValue(server);
    }

    public void delBookmark(Server server) {
        DatabaseReference bookmarkRef = databaseRef().child(TABLE_BOOKMARK_SERVERS).child(server.getIp());
        bookmarkRef.removeValue();
    }

    public List<Server> getBookmarks() {
        List<Server> serverList = new ArrayList<>();
        DatabaseReference bookmarkRef = databaseRef().child(TABLE_BOOKMARK_SERVERS);
        bookmarkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serverList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Server server = snapshot.getValue(Server.class);
                    serverList.add(server);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DLog.handleException(databaseError.toException());
                DLog.d("Database Error: " + databaseError.getMessage());
            }
        });

        return serverList;
    }

    public boolean checkBookmark(Server server) {
        DatabaseReference bookmarkRef = databaseRef().child(TABLE_BOOKMARK_SERVERS).child(server.getIp());
        return false;//@@@bookmarkRef.exists();
    }

    public void putLine(String line, int type) {
        String[] data = line.split(",");
        if (data.length == 15) {
            Server server = new Server(
                    data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
                    data[8], data[9], data[10], data[11], data[12], data[13], data[14], type
            );
            DatabaseReference serverRef = databaseRef().child(TABLE_SERVERS).child(data[1]);
            serverRef.setValue(server);
        }
    }

    public long getCount() {
        DatabaseReference serverRef = databaseRef().child(TABLE_SERVERS);
        return 134;//serverRef.getChildrenCount();
    }

    public long getCountBasic() {
        DatabaseReference serverRef = databaseRef().child(TABLE_SERVERS);
        Query basicQuery = serverRef.orderByChild("type").equalTo(0);
        return basicQuery.get().getResult().getChildrenCount();
    }

    public long getCountAdditional() {
        DatabaseReference serverRef = databaseRef().child(TABLE_SERVERS);
        Query additionalQuery = serverRef.orderByChild("type").equalTo(1);
        return additionalQuery.get().getResult().getChildrenCount();
    }

    @Override
    public void getServersByCountryCode(String aa, DataCallback<List<Server>> callback) {
        List<Server> countryList = new ArrayList<>();
        try {
            DatabaseReference serverRef = databaseRef().child(TABLE_SERVERS);
            Query query = serverRef.orderByChild("countryShort")
                    .equalTo(aa);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    countryList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Server server = snapshot.getValue(Server.class);
                        countryList.add(server);
                    }
                    callback.successResult(countryList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DLog.handleException(databaseError.toException());
                    callback.showError("Database Error: " + databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            callback.showError("ww " + e.getLocalizedMessage());
        }
    }

    @Override
    public void getUniqueCountries(DataCallback<List<Server>> callback) {
        List<Server> countryList = new ArrayList<>();
        try {
            DatabaseReference serverRef = databaseRef().child("countries");
            Query uniqueCountriesQuery = serverRef
                    //.orderByChild("quality")
                    //.limitToLast(1)
                    .orderByChild("countryShort");

            uniqueCountriesQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    countryList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Server server = snapshot.getValue(Server.class);
                        countryList.add(server);
                    }
                    callback.successResult(countryList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DLog.handleException(databaseError.toException());
                    callback.showError("Read Database Error: " + databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            DLog.handleException(e);
            callback.showError(e.getLocalizedMessage());
        }
    }
}
