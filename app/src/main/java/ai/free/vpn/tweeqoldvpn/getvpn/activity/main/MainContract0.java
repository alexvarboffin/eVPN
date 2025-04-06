package ai.free.vpn.tweeqoldvpn.getvpn.activity.main;

import com.walhalla.vpnapp.model.Server;

import java.util.List;

public interface MainContract0 {
    interface View {
        void showConfigs(List<Server> configList);
        void showError(String message);
    }

    interface Presenter {
        void loadConfigs();
        void applyConfig(Server configFileName);
    }
}

