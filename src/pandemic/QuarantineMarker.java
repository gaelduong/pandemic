package pandemic;

public class QuarantineMarker extends Unit {

    private QuarantineMarkerFaceValue upFace;
    private QuarantineMarkerFaceValue downFace;
    private boolean used;

    public QuarantineMarker() {
        used = false;
        upFace = QuarantineMarkerFaceValue.TWO;
        downFace = QuarantineMarkerFaceValue.ONE;
        setLocation(null);
        setUnitType(UnitType.QuarantineMarker);
    }

    public QuarantineMarkerFaceValue getUpFace() {
        return upFace;
    }

    public boolean flipMarker() {
        boolean upFaceStatus = false;
        upFace = oppositeFaceValue(upFace);
        downFace = oppositeFaceValue(downFace);

        if (upFace == QuarantineMarkerFaceValue.TWO) {
            upFaceStatus = true;
        }
        return upFaceStatus;
    }

    private QuarantineMarkerFaceValue oppositeFaceValue(QuarantineMarkerFaceValue val) {
        return val == QuarantineMarkerFaceValue.ONE ?
                QuarantineMarkerFaceValue.TWO : QuarantineMarkerFaceValue.ONE;
    }

    public void resetMarkerFace() {
        if(this.upFace == QuarantineMarkerFaceValue.ONE)
            flipMarker();
    }

}
