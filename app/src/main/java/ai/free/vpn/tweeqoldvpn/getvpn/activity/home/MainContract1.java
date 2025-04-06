package ai.free.vpn.tweeqoldvpn.getvpn.activity.home;

public class MainContract1 {
    public interface View {
        void showNoVPNConnected();
        void showVPNConnected();
        void showTotalServers(long totalServers);
        // Остальные методы для отображения данных и взаимодействия с UI
    }


    interface Presenter {
        void onViewCreated();
        void onViewResumed();
        void onDestroy();
        void onRandomConnectionButtonClicked();
        void onChooseCountryButtonClicked();
        void onSpeedTestButtonClicked();
        // Остальные методы для обработки событий и взаимодействия с моделью
    }
}
