package ga.kit.views;

import ga.kit.Chromosome;

/**
 * A view that interprets chromosomes as a (possibly variable length) string
 * of fixed size genes, encoding integer numeric values.
 */
public class IntegerFixedGeneView extends StandardView {
    protected int geneSize;
    protected boolean signed;
    protected long signMask;
    protected long extendMask;

    /**
     * Define the view, indicating the size of each gene and whether or not
     * the values encoded in the genes should be interpreted as signed.
     */
    public IntegerFixedGeneView(int geneSize, boolean signed) {
        if (geneSize > 64) {
            geneSize = 64;
        } else if (geneSize < 1) {
            geneSize = 1;
        }

        this.geneSize = geneSize;
        this.signed = signed;

        if (signed) {
            signMask = 1 << (geneSize - 1);
            extendMask = 0x8000000000000000L >> (64 - geneSize);
        }
    }

    /**
     * Get the value of gene <code>index</code>.  The result is a Long
     * holding the value encoded by that gene.
     */
    public Object getGene(Chromosome chromosome, int index) {
        long retValue = chromosome.getLongAt(geneSize * index, geneSize);

        if (signed && (retValue & signMask) != 0) {
            retValue |= extendMask;
        }

        return new Long(retValue);
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
