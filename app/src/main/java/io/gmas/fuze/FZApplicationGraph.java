package io.gmas.fuze;

import io.gmas.fuze.commons.Shark;
import io.gmas.fuze.commons.SharkTrackingClient;
import io.gmas.fuze.commons.session.Session;

public interface FZApplicationGraph {

    Session session();
    void inject(Shark __);
    void inject(SharkTrackingClient __);
    void inject(FZApplication __);

}
