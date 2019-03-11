package Application;

class GraphException extends RuntimeException
{

    GraphException(String cause)
    {
        AlertMessage.infoBox("Tried to " + cause);
    }
}
