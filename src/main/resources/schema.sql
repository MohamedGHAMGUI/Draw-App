-- Drop existing tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS graph_edges;
DROP TABLE IF EXISTS graph_nodes;
DROP TABLE IF EXISTS drawing_shapes;
DROP TABLE IF EXISTS drawings;

-- Create drawings table
CREATE TABLE drawings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_drawing_name (name),
    INDEX idx_drawing_created_at (created_at)
) ENGINE=InnoDB;

-- Create drawing_shapes table
CREATE TABLE drawing_shapes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    drawing_id INT NOT NULL,
    shape_type VARCHAR(50) NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    width DOUBLE,
    height DOUBLE,
    color VARCHAR(20),
    FOREIGN KEY (drawing_id) REFERENCES drawings(id) ON DELETE CASCADE,
    INDEX idx_drawing_shapes_drawing_id (drawing_id)
) ENGINE=InnoDB;

-- Create graph_nodes table
CREATE TABLE graph_nodes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    drawing_id INT NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    color VARCHAR(20),
    FOREIGN KEY (drawing_id) REFERENCES drawings(id) ON DELETE CASCADE,
    INDEX idx_graph_nodes_drawing_id (drawing_id)
) ENGINE=InnoDB;

-- Create graph_edges table
CREATE TABLE graph_edges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    drawing_id INT NOT NULL,
    source_node_id INT NOT NULL,
    target_node_id INT NOT NULL,
    weight DOUBLE NOT NULL,
    color VARCHAR(20),
    FOREIGN KEY (drawing_id) REFERENCES drawings(id) ON DELETE CASCADE,
    FOREIGN KEY (source_node_id) REFERENCES graph_nodes(id) ON DELETE CASCADE,
    FOREIGN KEY (target_node_id) REFERENCES graph_nodes(id) ON DELETE CASCADE,
    INDEX idx_graph_edges_drawing_id (drawing_id),
    INDEX idx_graph_edges_source (source_node_id),
    INDEX idx_graph_edges_target (target_node_id)
) ENGINE=InnoDB;

-- Add comments to tables
ALTER TABLE drawings COMMENT = 'Stores the main drawing information';
ALTER TABLE drawing_shapes COMMENT = 'Stores the shapes associated with each drawing';
ALTER TABLE graph_nodes COMMENT = 'Stores the nodes of the graph in each drawing';
ALTER TABLE graph_edges COMMENT = 'Stores the edges connecting nodes in the graph';

-- Add constraints
ALTER TABLE drawing_shapes
ADD CONSTRAINT check_positive_coordinates CHECK (x >= 0 AND y >= 0);

ALTER TABLE graph_nodes
ADD CONSTRAINT check_positive_node_coordinates CHECK (x >= 0 AND y >= 0);

ALTER TABLE graph_edges
ADD CONSTRAINT check_positive_weight CHECK (weight > 0); 