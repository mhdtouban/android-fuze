package io.gmas.fuze;

public class FZApplicationTest extends FZApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean isInUnitTests() {
        return true;
    }

}
