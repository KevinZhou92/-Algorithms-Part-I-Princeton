import edu.princeton.cs.algs4.*;
import java.awt.Color;

public class SeamCarver {
    private Color[][] pixels;
    private double[][] energy;
    private int height;
    private int width;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        height = picture.height();
        width = picture.width();
        pixels = new Color[height][width];
        energy = new double[height][width];
        
        // get color(r, g, b) for each pixel
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = picture.get(j, i);
            }
        }
        
        // calculate energy for each pixel
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                energy[i][j] = energy(j, i);
            }
        }
    }
    
    // width of current picture
    public int width() {
        return pixels[0].length;
    }
    // height of current picture
    public int height() {
        return pixels.length;
    }
    
    // reproduce current picture
    public Picture picture() {
        int newHeight = pixels.length;
        int newWidth = pixels[0].length;
        Picture output = new Picture(newWidth, newHeight);
        
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                output.set(i, j, pixels[j][i]);
            }
        }
        return output;
    }
    
    // energy of pixel at column x and row y
    public  double energy(int col, int row) {
        checkRange(col, row);
        if (col == 0 || col == width() - 1 || row == 0 || row == height() - 1) {
            return (double)1000;
        }
        return Math.sqrt(horizontal_energy(col, row) + vertical_energy(col, row));
    }
    
    private double horizontal_energy(int x, int y) {
        double energy1 = pixels[y][x - 1].getRed() - pixels[y][x + 1].getRed();
        double energy2 = pixels[y][x - 1].getBlue() - pixels[y][x + 1].getBlue();
        double energy3 = pixels[y][x - 1].getGreen() - pixels[y][x + 1].getGreen();
        
        return energy1 * energy1 + energy2 * energy2 + energy3 * energy3;
    }
    
    private double vertical_energy(int x, int y) {
        double energy1 = pixels[y - 1][x].getRed() - pixels[y + 1][x].getRed();
        double energy2 = pixels[y - 1][x].getBlue() - pixels[y + 1][x].getBlue();
        double energy3 = pixels[y - 1][x].getGreen() - pixels[y + 1][x].getGreen();
        
        return energy1 * energy1 + energy2 * energy2 + energy3 * energy3;
    }
    
    // sequence of indices for horizontal seam
    public int[] findVerticalSeam() {
        int[] pos_index = new int[energy.length];
        double[][] energySum = new double[energy.length][energy[0].length];
        
        // initialize the energy weight on each vertex
        for (int i = 0; i < energy.length; i++) {
            for (int j = 0; j < energy[0].length; j++) {
                if (i == 0) {
                    energySum[i][j] = (double)1000;
                    continue;
                }
                energySum[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        
        for (int row = 0; row < energy.length - 1; row++) {
            for (int col = 0; col < energy[0].length; col++) {
                if (col > 0) {
                    if (energySum[row][col] + energy[row + 1][col - 1] < energySum[row + 1][col - 1]) {
                        energySum[row + 1][col - 1] = energySum[row][col] + energy[row + 1][col - 1];
                    }
                }
                
                if (energySum[row][col] + energy[row + 1][col] < energySum[row + 1][col]) {
                    energySum[row + 1][col] = energySum[row][col] + energy[row + 1][col];
                }
                
                if (col < energy[0].length - 1) {
                    if (energySum[row][col] + energy[row + 1][col + 1] < energySum[row + 1][col + 1]) {
                        energySum[row + 1][col + 1] = energySum[row][col] + energy[row + 1][col + 1];
                    }
                }
            }
        }
        
        // find the minimum sum and the index in the row
        double minSum = Double.POSITIVE_INFINITY;
        for (int i = 0; i < energy[0].length; i++) {
            if (energySum[energy.length - 1][i] < minSum) {
                minSum = energySum[energy.length - 1][i];
                pos_index[energySum.length - 1] = i;
            }
        }
        // reconstruct the path
        for (int i = energySum.length - 2; i >= 0; i--) {
            int preCol = pos_index[i + 1];
            for (int k = -1; k <= 1; k++) {
                if ((preCol + k) >= 0 && (preCol + k) < energy[0].length) {
                    if (Math.abs(energySum[i + 1][preCol] - energy[i + 1][preCol] - energySum[i][preCol + k]) < 0.00000000009 ) {
                        pos_index[i] = preCol + k;
                        break;
                    }
                }
            }
        }
        
        return pos_index;
    }
    
    // transpose the energy matrix
    private void transpose() {
        int width = energy[0].length;
        int height = energy.length;
        double[][] transpose = new double[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                transpose[j][i] = energy[i][j];
            }
        }
        energy = transpose;
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] horizontal = findVerticalSeam();
        transpose();
        return horizontal;
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkSeam(seam, false);
        Color[][]result = new Color[pixels.length - 1][pixels[0].length];
        double[][]energyResult = new double[pixels.length - 1][pixels[0].length];
        for (int i = 0; i < pixels[0].length; i++) {
            int k = 0;
            for (int j = 0; j < pixels.length; j++) {
                if ( j != seam[i]) {
                    result[k][i] = pixels[j][i];
                    k++;
                }
            }
        }
        pixels = result;
        
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length ; j++) {
                energyResult[i][j] = energy(j, i);
            }
        }
        energy = energyResult;
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkSeam(seam, true);
        Color[][]result = new Color[pixels.length][pixels[0].length - 1];
        double[][]energyResult = new double[pixels.length][pixels[0].length - 1];
        for (int i = 0; i < pixels.length; i++) {
            int k = 0;
            for (int j = 0; j < pixels[0].length; j++) {
                if (j != seam[i]) {
                    result[i][k] = pixels[i][j];
                    k++;
                }
            }
        }
        pixels = result;
        
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                energyResult[i][j] = energy(j, i);
            }
        }
        energy = energyResult;
    }
    
    private void checkRange(int x, int y) {
        if (x < 0 || x > width - 1) {
            throw new IndexOutOfBoundsException("Check your input!");
        }
        if (y < 0 || y > height - 1) {
            throw new IndexOutOfBoundsException("Check your input!");
        }
    }
    
    private void checkSeam(int[] seam, boolean isVertical) {
        if (seam[0] < 0) {
            throw new IllegalArgumentException("Check it!");
        }
        if (isVertical) {
            if (pixels.length != seam.length) {
                throw new IllegalArgumentException("Check it!");
            }
            for (int i = 0; i < seam.length - 1; i++) {
                if (seam[i] < 0 || seam[i + 1] < 0) {
                    throw new IllegalArgumentException("Check it!");
                }
                if (seam[i + 1] == pixels[0].length || seam[i] == pixels[0].length || Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException("Check it!");
                }
            }
        } else {
            if (pixels[0].length != seam.length) {
                throw new IllegalArgumentException("Check it!");
            }
            for (int i = 0; i < seam.length - 1; i++) {
                if (seam[i] < 0 || seam[i + 1] < 0) {
                    throw new IllegalArgumentException("Check it!");
                }
                if (seam[i + 1] == pixels.length || seam[i] == pixels.length || Math.abs(seam[i] - seam[i + 1]) > 1) {
                    throw new IllegalArgumentException("Check it!");
                }
            }
        }
    }
    
    // unit test
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        
        
        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.2f ", sc.energy(col, row));
            StdOut.println();
        }
        
        for (int x : sc.findHorizontalSeam()) {
            System.out.println(x);
        }
    }
}
