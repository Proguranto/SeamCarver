package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.ShortestPathSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Generative adjacency list graph single-source {@link ShortestPathSolver} implementation of the {@link SeamFinder}
 * interface.
 *
 * @see Graph
 * @see ShortestPathSolver
 * @see SeamFinder
 * @see SeamCarver
 */
public class GenerativeSeamFinder implements SeamFinder {
    /**
     * The constructor for the {@link ShortestPathSolver} implementation.
     */
    private final ShortestPathSolver.Constructor<Node> sps;

    /**
     * Constructs an instance with the given {@link ShortestPathSolver} implementation.
     *
     * @param sps the {@link ShortestPathSolver} implementation.
     */
    public GenerativeSeamFinder(ShortestPathSolver.Constructor<Node> sps) {
        this.sps = sps;
    }

    @Override
    public List<Integer> findSeam(Picture picture, EnergyFunction f) {
        PixelGraph graph = new PixelGraph(picture, f);
        List<Node> seam = sps.run(graph, graph.source).solution(graph.sink);
        seam = seam.subList(1, seam.size() - 1); // Skip the source and sink nodes
        List<Integer> result = new ArrayList<>(seam.size());
        for (Node pixel : seam) {
            result.add(((PixelGraph.Pixel) pixel).y);
        }
        return result;
    }

    /**
     * Generative adjacency list graph of {@link Pixel} vertices and {@link EnergyFunction}-weighted edges. Rather than
     * materialize all vertices and edges upfront in the constructor, generates vertices and edges as needed when
     * {@link #neighbors(Node)} is called by a client.
     *
     * @see Pixel
     * @see EnergyFunction
     */
    private static class PixelGraph implements Graph<Node> {
        /**
         * The {@link Picture} for {@link #neighbors(Node)}.
         */
        private final Picture picture;
        /**
         * The {@link EnergyFunction} for {@link #neighbors(Node)}.
         */
        private final EnergyFunction f;
        /**
         * Source {@link Node} for the adjacency list graph.
         */
        private final Node source = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> neighbors = new ArrayList<>(picture.height());
                for (int y = 0; y < picture.height(); y++) {
                    Pixel to = new Pixel(0, y);
                    neighbors.add(new Edge<>(this, to, f.apply(picture, 0, y)));
                }
                return neighbors;
            }
        };
        /**
         * Sink {@link Node} for the adjacency list graph.
         */
        private final Node sink = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                return List.of();   // sink has no neighbors
            }
        };

        /**
         * Constructs a generative adjacency list graph. All work is deferred to implementations of
         * {@link Node#neighbors(Picture, EnergyFunction)}.
         *
         * @param picture the input picture.
         * @param f       the input energy function.
         */
        private PixelGraph(Picture picture, EnergyFunction f) {
            this.picture = picture;
            this.f = f;
        }

        @Override
        public List<Edge<Node>> neighbors(Node node) {
            return node.neighbors(picture, f);
        }

        /**
         * A pixel in the {@link PixelGraph} representation of the {@link Picture} with {@link EnergyFunction}-weighted
         * edges to neighbors.
         *
         * @see PixelGraph
         * @see Picture
         * @see EnergyFunction
         */
        public class Pixel implements Node {
            private final int x;
            private final int y;

            /**
             * Constructs a pixel representing the (<i>x</i>, <i>y</i>) indices in the picture.
             *
             * @param x horizontal index into the picture.
             * @param y vertical index into the picture.
             */
            public Pixel(int x, int y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> result = new ArrayList<>(picture.height());
                // pixel on right-most edge
                if (this.x == picture.width() - 1) {
                    result.add(new Edge<Node>(this, sink, 0));
                } else {
                    // creating a pixel and edge for the right-up, right-middle, right-down neighbors..
                    for (int z = this.y - 1; z <= this.y + 1; z++) {
                        if (0 <= z && z < picture.height()) {
                            Pixel to = new Pixel(x + 1, z);
                            result.add(new Edge<>(this, to, f.apply(picture, x + 1, z)));
                        }
                    }
                }
                return result;
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (!(o instanceof Pixel)) {
                    return false;
                }
                Pixel other = (Pixel) o;
                return this.x == other.x && this.y == other.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
    /**
     * This is our experimental analysis for our project
     * Here we can see a graph of the overal comparison for all the implementations
     * I've labeled series 1-5 on the side and which implementation they correspond to
     * and showed the rankings from fastest to slowest
     * where Dynamic Programming being the fastest, followed by Dijkstra, and lastly Toppsort
     * ---
     * Here I have a graph representation for the Dijkstra vs Toposort solver using the Adjacency List
     * Implementation
     * This is also a graph representation for Dijkstra vs Toposort but using the Generative Seam Finder Implementation
     * The next thing I would like to point out is the comparison between the Generative Seam Finder vs
     * Adjacency List implementation 
     * As we can see for Dijkstra Adjacency List is faster than Generative Seam Finder
     * and for Toposort Adjacency List is also faster Generative Seam finder, but by a larger margin
     * 
     */
}
