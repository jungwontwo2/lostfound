package baseball.lostfound.domain.enums;

public enum Position {
    FIRST_BASE("1루"),THIRD_BASE("3루"),OUT_FIELD("외야"),ETC("기타");

    private final String positionName;

    Position(String positionName){
        this.positionName=positionName;
    }
    public String getPositionName(){
        return positionName;
    }
}
