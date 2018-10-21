package models.torrent.deTorrent;

/**
 * Created by SpereShelde on 2018/10/18.
 */
public class DEResponse {

    private int id;
    private Result result;
    private int error;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
