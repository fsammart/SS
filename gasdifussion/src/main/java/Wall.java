public enum Wall {
    // Wall Direction
    HORIZONTAL,
    VERTICAL,
    MIDDLE_VERTICAL;

    private double length;

    public void setLength(final double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }

}
