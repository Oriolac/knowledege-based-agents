package apryraz.tworld.data;

public class TFState {
    /**
     *
     **/

    int wDim;
    String[][] matrix;

    public TFState(int dim) {
        wDim = dim;
        matrix = new String[wDim][wDim];
        initializeState();
    }

    public void initializeState() {
        for (int i = 0; i < wDim; i++) {
            for (int j = 0; j < wDim; j++) {
                matrix[i][j] = "?";
            }
        }
    }

    public void set(int i, int j, String val) {
        matrix[i - 1][j - 1] = val;
    }

    public void set(Position position, String val) {
        set(position.getX(), position.getY(), val);
    }

    public boolean equals(Object obj) {
        TFState tfstate2 = (TFState) obj;

        for (int i = 0; i < wDim; i++) {
            for (int j = 0; j < wDim; j++) {
                if (!matrix[i][j].equals(tfstate2.matrix[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    public void printState() {
        System.out.println("FINDER => Printing Treasure world matrix");
        for (int j = wDim - 1; j > -1; j--) {
            System.out.print("\t#\t");
            for (int i = 0; i < wDim; i++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("\t#");
        }
    }

}
