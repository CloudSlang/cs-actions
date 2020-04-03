package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public class Region {
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;


    public Region(int left, int top, int right, int bottom) {
        if ((left > right || top > bottom) ||
                (left < 0 && left != -1) ||
                (top < 0 && top != -1) ||
                (right < 0 && right != -1) ||
                (bottom < 0 && bottom != -1)) {
            throw new IllegalArgumentException(ExceptionMsgs.INVALID_REGION);
        }
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }


    public int getLeft() {
        return left;
    }


    public int getTop() {
        return top;
    }


    public int getRight() {
        return right;
    }


    public int getBottom() {
        return bottom;
    }
}
