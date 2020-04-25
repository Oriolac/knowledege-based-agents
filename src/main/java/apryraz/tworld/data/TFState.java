package apryraz.tworld.data;

/**
 * class to create and print the state of the world (represented y a matrix)
 * after every step
 */
public class TFState {

    int wDim;
    String[][] matrix;

    /**
     * constructor of the class TFState
     * @param dim size of the world
     */
    public TFState(int dim) {
        wDim = dim;
        matrix = new String[wDim][wDim];
        initializeState();
    }

    /**
     * initialize all the positions of the matrix as unknown (?)
     */
    public void initializeState() {
        for (int i = 0; i < wDim; i++) {
            for (int j = 0; j < wDim; j++) {
                matrix[i][j] = "?";
            }
        }
    }

    /**
     * set a value in a determinate position (i,j) of the matrix
     * @param i coordinate x of the matrix
     * @param j coordinate y of the matrix
     * @param val value to be set in the position
     */
    public void set(int i, int j, String val) {
        matrix[i - 1][j - 1] = val;
    }

    /**
     * set a value in a determinate position of the matrix
     * @param position position of the matrix
     * @param val value to be set in the position
     */
    public void set(Position position, String val) {
        set(position.getX(), position.getY(), val);
    }
//TODO: no se com explicar-la
    public boolean equals(Object obj) {
        if (!(obj instanceof TFState))
            return false;
        TFState tfstate2 = (TFState) obj;
        if (wDim != ((TFState) obj).wDim)
            return false;
        for (int i = 0; i < wDim; i++) {
            for (int j = 0; j < wDim; j++) {
                if (!matrix[i][j].equals(tfstate2.matrix[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * print on the screen the state of the world
     */
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
