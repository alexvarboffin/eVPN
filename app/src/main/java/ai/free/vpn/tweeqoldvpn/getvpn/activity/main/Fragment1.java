package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ai.free.vpn.tweeqoldvpn.getvpn.R;
import com.walhalla.vpnapp.model.Server;
import com.walhalla.ui.DLog;

import java.util.List;

public class Fragment1 extends Fragment
        implements MainContract0.View, ConfigAdapter.Clb {
    private MainContract0.Presenter presenter;

    private ConfigAdapter configAdapter;

    @Override
    public void showConfigs(List<Server> configList) {
        configAdapter.setConfigs(configList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simle_main, container, false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        DLog.d("@@@" + message);
    }


    @Override
    public void onItemClick(Server server) {
        DLog.d("@@@");


        presenter.applyConfig(server);
        // Creates a button that mimics a crash when pressed

        try {
            // Server server=makeServer();
            // Установка обработчика событий OpenVPN
//            VpnStatus.addStateListener(new VpnStatus.StateListener() {
//                @Override
//                public void updateState(String state, String logmessage, int localizedResId, ConnectionStatus level, Intent Intent) {
//                    if (level == ConnectionStatus.CONNECTED) {
//                        // Обработка успешного подключения
//                        // Можете выполнить дополнительные действия или показать сообщение пользователю
//                    } else if (level == ConnectionStatus.DISCONNECTED) {
//                        // Обработка отключения
//                    } else if (level == ConnectionStatus.PAUSED) {
//                        // Обработка приостановки
//                    } else if (level == ConnectionStatus.RESUMED) {
//                        // Обработка возобновления
//                    }
//                }
//
//                @Override
//                public void setConnectedVPN(String uuid) {
//
//                }
//
//            });

            // Очистка существующего профиля VPN
//            ProfileManager.getInstance(getContext()).stopVPN();
//            ProfileManager.getInstance(getContext()).removeProfile(vp);
//
//            // Добавление нового профиля VPN
//            ProfileManager.getInstance(view.getContext()).addProfile(vp);
//            ProfileManager.getInstance(view.getContext()).saveProfile(vp);
//
//            // Подключение к VPN-серверу
//            Intent intent = new Intent(getContext(), OpenVPNService.class);
//            getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
//            getContext().startService(intent);

            //@@@ vpnProfile0 = VpnUtils.loadVpnProfile(this, currentServer);

        } catch (Exception e) {
            DLog.handleException(e);
        }


    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        configAdapter = new ConfigAdapter(this);
        recyclerView.setAdapter(configAdapter);
        presenter = new MainPresenter0(getActivity(), this);
        presenter.loadConfigs();
    }
}
