package ga.kit.views;

import ga.kit.Chromosome;

/**
 * A view that interprets chromosomes as a (possibly variable length) string
 * of fixed size genes, encoding floating point values in a given range.
 */
public class FloatFixedGeneView extends StandardView {
    protected int geneSize;
    protected double start;
    protected double scale;

    /**
     * Define the view, indicating the size of each gene.  The value
     * returned from the gene will be a Double in the range
     * <code>start</code> to <code>start + range</code>.
     */
    public FloatFixedGeneView(int geneSize, double start, double range) {
        this.geneSize = geneSize;
        this.start = start;

        // Compute max value for that gene size and from that derive a
        // scaling factor that will ensure that all returned values fall
        // within the required range.

        this.scale = range / (double) ((1L << geneSize) - 1);
    }

    /**
     * Get the value of gene <code>index</code>.  The result is a Double
     * holding the value encoded by that gene interpreted to fall within the
     * required range.
     */
    public Object getGene(Chromosome chromosome, int index) {
        return new Double(chromosome.getLongAt(geneSize * index, geneSize) *
                scale + start);
    }

    /**
     * Return the number of genes.
     */
    public int numberOfGenes(Chromosome chromosome) {
        return chromosome.size() / geneSize;
    }

    /**
     * Returns a stringified representation of a chromosome as viewed
     * through this view.
     */
    public String toString(Chromosome chromosome) {
        StringBuffer buf = new StringBuffer();

        buf.append(getGene(chromosome, 0));
        for (int i = 1; i < numberOfGenes(chromosome); ++i) {
            buf.append(",");
            buf.append(getGene(chromosome, i));
        }
        return buf.toString();
    }
}
