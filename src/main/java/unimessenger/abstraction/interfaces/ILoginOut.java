package unimessenger.abstraction.interfaces;

public interface ILoginOut
{
    boolean checkIfLoggedIn();
    boolean login();
    boolean logout();
    boolean needsRefresh();
    boolean refresh();
}