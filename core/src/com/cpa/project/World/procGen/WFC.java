
package com.cpa.project.World.procGen;


import com.badlogic.gdx.math.Vector2;
import com.cpa.project.Tiles.Tile;
import com.cpa.project.Tiles.terrainFloorTiles;

import java.util.*;
import java.util.Map;


class WFCNode {

    String name;
    Tile tile;
    WFCConnection Top; // les connections du haut de la tile qui sont possibles
    WFCConnection Bottom;
    WFCConnection Left;
    WFCConnection Right;

    double entropy;


    public WFCNode(String name, Tile tile , double entropy ) {
        this.name = name;
        this.tile = tile;
        this.entropy = entropy;
        this.Top = new WFCConnection();
        this.Bottom = new WFCConnection();
        this.Left = new WFCConnection();
        this.Right = new WFCConnection();

    }

    public WFCNode addTop(WFCNode node){
        Top.addNode(node);
//        node.Bottom.addNode(this);
        return this;
    }

    public WFCNode addBottom(WFCNode node){
        Bottom.addNode(node);
//        node.Top.addNode(this);
        return this;
    }

    public WFCNode addLeft(WFCNode node){
        Left.addNode(node);
//        node.Right.addNode(this);
        return this;
    }

    public WFCNode addRight(WFCNode node){
        Right.addNode(node);
//        node.Left.addNode(this);
        return this;
    }

    public void setEntropy(double entropy){
        this.entropy = entropy;
    }

    public double getEntropy(){
        return this.entropy;
    }


}

class WFCConnection {
    ArrayList<WFCNode> compatibleNodes ;
    public WFCConnection() {
        compatibleNodes = new ArrayList<>();
    }

    public void addNode(WFCNode node){
        if (!compatibleNodes.contains(node))
            compatibleNodes.add(node);
    }

    public void removeNode(WFCNode node){
        compatibleNodes.remove(node);
    }

    // print the compatible nodes
    public void printCompatibleNodes(){
        for (int i = 0; i < compatibleNodes.size(); i++) {
            System.err.println(compatibleNodes.get(i).name);
        }
    }
}

/**
 *  classe qui permet de gérer la map et les tiles de la map
 *  Algorithme de Wave Function Collapse : https://robertheaton.com/2018/12/17/wavefunction-collapse-algorithm/
 *  ATTEMPT AT IMPLEMENTING WAVE FUNCTION COLLAPSE with tileset of 3x3 of multiple types ...
 *  HOURS WASTED ON THIS : 14
 *  IT WORKS BUT IT'S NOT EFFICIENT SO I WILL NOT USE IT AND I WILL USE THE NOISE PROCEDURAL GENERATION INSTEAD ...
 */
public class WFC {
    // the Size of the map : width and height
    private int width,Height;

    // a 2D array of WFCNode that will store our collapsed tiles so we can reference them later
    private WFCNode[][] map;

    // the list of all the nodes that we have
    public ArrayList<WFCNode> nodes = new ArrayList<>();

    // the list of all the tiles that we will output
    ArrayList<Tile> outputTiles = new ArrayList<>();

    // a list of the vector2 that we will use to collapse the tiles
    private ArrayList<Vector2> toCollapse = new ArrayList<>();

    WFCNode[][][] chunks;

    // an array of the offsets that we will use to get the neighbors of a tile
    private Vector2[] offsets = new Vector2[]{
            new Vector2(0,1), // top
            new Vector2(0,-1), // bottom
            new Vector2(1,0), // right
            new Vector2(-1,0) // left
//            ,new Vector2(1,1), // top right
//            new Vector2(-1,1), // top left
//            new Vector2(1,-1), // bottom right
//            new Vector2(-1,-1) // bottom left
    };

    terrainFloorTiles terrainFloorTiles;

    Tile[][] sandCenterWGrass;
    Tile[][] grassCenterWSand;
    Tile[][] rockCenterWSand;
    Tile[][] voidCenterWRock;
    Tile[][] waterTiles;


    WFCNode centerStart ;

    int chunkSize = 16;

    // the constructor of the WFC
    public WFC(int width, int height) {
        this.width = width;
        this.Height = height;
        this.map = new WFCNode[width][height];
        this.terrainFloorTiles = new terrainFloorTiles();
        this.sandCenterWGrass = terrainFloorTiles.getTileBox(0);
        this.grassCenterWSand = terrainFloorTiles.getTileBox(10);
        this.rockCenterWSand = terrainFloorTiles.getTileBox(20);
        this.voidCenterWRock = terrainFloorTiles.getTileBox(30);
        this.waterTiles = terrainFloorTiles.getTileBox(40);

        initNodes();
        collapseWorld();

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return Height;
    }

    // this function will initialize the nodes that we have in the world
    // and the connections between them
    private void initNodes() {
        // we will start with sandCenterWGrass
        double outerEntropy = 3.5;
        double innerEntropy =28.1;
        double edgeEntropy = 1.14;

        Map<Integer,WFCNode> allNodes = new HashMap<>();
        allNodes.put(0, new WFCNode("sandCenterWGrass[0][0]", sandCenterWGrass[0][0] , edgeEntropy));
        allNodes.put(1, new WFCNode("sandCenterWGrass[0][1]", sandCenterWGrass[0][1] , outerEntropy));
        allNodes.put(2, new WFCNode("sandCenterWGrass[0][2]", sandCenterWGrass[0][2] , edgeEntropy));
        allNodes.put(3, new WFCNode("sandCenterWGrass[1][0]", sandCenterWGrass[1][0] , outerEntropy));
        allNodes.put(4, new WFCNode("sandCenterWGrass[1][1]", sandCenterWGrass[1][1] , innerEntropy));
        allNodes.put(5, new WFCNode("sandCenterWGrass[1][2]", sandCenterWGrass[1][2] , outerEntropy));
        allNodes.put(6, new WFCNode("sandCenterWGrass[2][0]", sandCenterWGrass[2][0] , edgeEntropy));
        allNodes.put(7, new WFCNode("sandCenterWGrass[2][1]", sandCenterWGrass[2][1] , outerEntropy));
        allNodes.put(8, new WFCNode("sandCenterWGrass[2][2]", sandCenterWGrass[2][2] , edgeEntropy));

        innerEntropy = 22.9;
        edgeEntropy = 1.14;
        outerEntropy = 3.5;
        allNodes.put(10, new WFCNode("grassCenterWSand[0][0]", grassCenterWSand[0][0] , edgeEntropy));
        allNodes.put(11, new WFCNode("grassCenterWSand[0][1]", grassCenterWSand[0][1] , outerEntropy));
        allNodes.put(12, new WFCNode("grassCenterWSand[0][2]", grassCenterWSand[0][2] , edgeEntropy));
        allNodes.put(13, new WFCNode("grassCenterWSand[1][0]", grassCenterWSand[1][0] , outerEntropy));
        allNodes.put(14, new WFCNode("grassCenterWSand[1][1]", grassCenterWSand[1][1] , innerEntropy));
        allNodes.put(15, new WFCNode("grassCenterWSand[1][2]", grassCenterWSand[1][2] , outerEntropy));
        allNodes.put(16, new WFCNode("grassCenterWSand[2][0]", grassCenterWSand[2][0] , edgeEntropy));
        allNodes.put(17, new WFCNode("grassCenterWSand[2][1]", grassCenterWSand[2][1] , outerEntropy));
        allNodes.put(18, new WFCNode("grassCenterWSand[2][2]", grassCenterWSand[2][2] , edgeEntropy));

        innerEntropy = 9.1;
        edgeEntropy = 0.95;
        outerEntropy = 0.5;
        allNodes.put(20, new WFCNode("rockCenterWSand[0][0]", rockCenterWSand[0][0] , edgeEntropy));
        allNodes.put(21, new WFCNode("rockCenterWSand[0][1]", rockCenterWSand[0][1] , outerEntropy));
        allNodes.put(22, new WFCNode("rockCenterWSand[0][2]", rockCenterWSand[0][2] , edgeEntropy));
        allNodes.put(23, new WFCNode("rockCenterWSand[1][0]", rockCenterWSand[1][0] , outerEntropy));
        allNodes.put(24, new WFCNode("rockCenterWSand[1][1]", rockCenterWSand[1][1] , innerEntropy));
        allNodes.put(25, new WFCNode("rockCenterWSand[1][2]", rockCenterWSand[1][2] , outerEntropy));
        allNodes.put(26, new WFCNode("rockCenterWSand[2][0]", rockCenterWSand[2][0] , edgeEntropy));
        allNodes.put(27, new WFCNode("rockCenterWSand[2][1]", rockCenterWSand[2][1] , outerEntropy));
        allNodes.put(28, new WFCNode("rockCenterWSand[2][2]", rockCenterWSand[2][2] , edgeEntropy));


        innerEntropy = 0.1;
        edgeEntropy = 0.1;
        outerEntropy = 0.1;
        allNodes.put(30, new WFCNode("voidCenterWRock[0][0]", voidCenterWRock[0][0] , edgeEntropy));
        allNodes.put(31, new WFCNode("voidCenterWRock[0][1]", voidCenterWRock[0][1] , outerEntropy));
        allNodes.put(32, new WFCNode("voidCenterWRock[0][2]", voidCenterWRock[0][2] , edgeEntropy));
        allNodes.put(33, new WFCNode("voidCenterWRock[1][0]", voidCenterWRock[1][0] , outerEntropy));
        allNodes.put(34, new WFCNode("voidCenterWRock[1][1]", rockCenterWSand[1][1] , innerEntropy));
        allNodes.put(35, new WFCNode("voidCenterWRock[1][2]", voidCenterWRock[1][2] , outerEntropy));
        allNodes.put(36, new WFCNode("voidCenterWRock[2][0]", voidCenterWRock[2][0] , edgeEntropy));
        allNodes.put(37, new WFCNode("voidCenterWRock[2][1]", voidCenterWRock[2][1] , outerEntropy));
        allNodes.put(38, new WFCNode("voidCenterWRock[2][2]", voidCenterWRock[2][2] , edgeEntropy));
//
//        allNodes.put(40, new WFCNode("waterTiles[0][0]", waterTiles[0][0] , 0.1));
//        allNodes.put(41, new WFCNode("waterTiles[0][1]", waterTiles[0][1] , 0.1));
//        allNodes.put(42, new WFCNode("waterTiles[0][2]", waterTiles[0][2] , 0.1));
//        allNodes.put(43, new WFCNode("waterTiles[1][0]", waterTiles[1][0] , 0.1));
//        allNodes.put(44, new WFCNode("waterTiles[1][1]", waterTiles[1][1] , 1.4));
//        allNodes.put(45, new WFCNode("waterTiles[1][2]", waterTiles[1][2] , 0.1));
//        allNodes.put(46, new WFCNode("waterTiles[2][0]", waterTiles[2][0] , 0.1));
//        allNodes.put(47, new WFCNode("waterTiles[2][1]", waterTiles[2][1] , 0.1));
//        allNodes.put(48, new WFCNode("waterTiles[2][2]", waterTiles[2][2] , 0.1));






        // we add the nodes to the nodes list
        nodes.addAll(allNodes.values());

        centerStart = allNodes.get(4);


        setConnections(allNodes, 0);
        setConnections(allNodes,  10);
        setConnections(allNodes,  20);
        setConnections(allNodes,  30);
//        setConnections(allNodes,  40);

//         we set the outer connections
        setOuterConnections(allNodes,  0, 10);
        setOuterConnections(allNodes , 10, 0);
        setOuterConnections(allNodes,  10, 20);
        setOuterConnections(allNodes,  20, 10);
        setOuterConnections(allNodes,  20, 30);
        setOuterConnections(allNodes,  30, 20);
//        setOuterConnections(allNodes,  40, 20);
//        setOuterConnections(allNodes,  40, 0);
//        setOuterConnections(allNodes,  0, 40);
//        setOuterConnections(allNodes,  10, 40);
//        setOuterConnections(allNodes,  40, 10);
//        setOuterConnections(allNodes,  20, 40);
//        setOuterConnections(allNodes,  40, 30);

        System.err.println("Top" ) ;
        allNodes.get(4).Top.printCompatibleNodes();
        System.err.println("Bottom" ) ;
        allNodes.get(4).Bottom.printCompatibleNodes();
        System.err.println("Left" ) ;
        allNodes.get(4).Left.printCompatibleNodes();
        System.err.println("Right" ) ;
        allNodes.get(4).Right.printCompatibleNodes();

    }

    void setConnections(Map<Integer, WFCNode> allNodes, int baseIndex) {
        allNodes.get(baseIndex+4).addRight(allNodes.get(baseIndex+4))
                .addBottom(allNodes.get(baseIndex+4)).addLeft(allNodes.get(baseIndex+4))
                .addTop(allNodes.get(baseIndex+4));

        allNodes.get(baseIndex).addRight(allNodes.get(baseIndex + 1)).
                addBottom(allNodes.get(baseIndex + 3));
        allNodes.get(baseIndex + 1).addLeft(allNodes.get(baseIndex)).
                addRight(allNodes.get(baseIndex + 2)).
                addBottom(allNodes.get(baseIndex + 4))
//                .addLeft(allNodes.get(baseIndex + 1)).addRight(allNodes.get(baseIndex + 1))
        ;
        allNodes.get(baseIndex + 2).addLeft(allNodes.get(baseIndex + 1)).
                addBottom(allNodes.get(baseIndex + 5));

        allNodes.get(baseIndex + 3).addTop(allNodes.get(baseIndex)).
                addRight(allNodes.get(baseIndex + 4)).
                addBottom(allNodes.get(baseIndex + 6))
//                .addTop(allNodes.get(baseIndex + 3)).addBottom(allNodes.get(baseIndex + 3))
        ;
        allNodes.get(baseIndex + 4).addTop(allNodes.get(baseIndex + 1))
                .addLeft(allNodes.get(baseIndex + 3))
                .addRight(allNodes.get(baseIndex + 5))
                .addBottom(allNodes.get(baseIndex + 7));
        allNodes.get(baseIndex + 5).addTop(allNodes.get(baseIndex + 2))
                .addLeft(allNodes.get(baseIndex + 4))
                .addBottom(allNodes.get(baseIndex + 8))
//                .addTop(allNodes.get(baseIndex + 5)).addBottom(allNodes.get(baseIndex + 5))
        ;

        allNodes.get(baseIndex + 6).addTop(allNodes.get(baseIndex + 3))
                .addRight(allNodes.get(baseIndex + 7));
        allNodes.get(baseIndex + 7).addTop(allNodes.get(baseIndex + 4))
                .addLeft(allNodes.get(baseIndex + 6))
                .addRight(allNodes.get(baseIndex + 8))
//                .addRight(allNodes.get(baseIndex + 7)).addLeft(allNodes.get(baseIndex + 7))
        ;
        allNodes.get(baseIndex + 8).addTop(allNodes.get(baseIndex + 5))
                .addLeft(allNodes.get(baseIndex + 7));

    }


    public void setOuterConnections(Map<Integer, WFCNode> allNodes, int baseIndex1, int baseIndex2) {
//        // top left
        allNodes.get(baseIndex1).addLeft(allNodes.get(baseIndex2+4)).addTop(allNodes.get(baseIndex2+4))
                .addTop(allNodes.get(baseIndex2+1)).addLeft(allNodes.get(baseIndex2+3))
        ;

        // top right
        allNodes.get(baseIndex1+2).addRight(allNodes.get(baseIndex2+4)).addTop(allNodes.get(baseIndex2+4))
                .addTop(allNodes.get(baseIndex2+1)).addRight(allNodes.get(baseIndex2+5))
        ;

        // top
        allNodes.get(baseIndex1+1).addTop(allNodes.get(baseIndex2+4))
                .addTop(allNodes.get(baseIndex2+1))
        ;

        // bottom left
        allNodes.get(baseIndex1+6).addLeft(allNodes.get(baseIndex2+4)).addBottom(allNodes.get(baseIndex2+4))
                .addBottom(allNodes.get(baseIndex2+7)).addLeft(allNodes.get(baseIndex2+3))
        ;

        // bottom right
        allNodes.get(baseIndex1+8).addRight(allNodes.get(baseIndex2+4)).addBottom(allNodes.get(baseIndex2+4))
                .addBottom(allNodes.get(baseIndex2+7))
                .addRight(allNodes.get(baseIndex2+5))
        ;

        // bottom
        allNodes.get(baseIndex1+7).addBottom(allNodes.get(baseIndex2+4))
                .addBottom(allNodes.get(baseIndex2+7))
        ;

        // left
        allNodes.get(baseIndex1+3).addLeft(allNodes.get(baseIndex2+4))
                .addLeft(allNodes.get(baseIndex2+3))
        ;

        // right
        allNodes.get(baseIndex1+5).addRight(allNodes.get(baseIndex2+4))
                .addRight(allNodes.get(baseIndex2+5))
                ;

    }

    // divide the world into chunks and initialize the chunks arrays
    private WFCNode[][][] divideIntoChunks() {
        int chunksX = width / chunkSize;
        int chunksY = Height / chunkSize;
        WFCNode[][][] chunks = new WFCNode[chunksX][chunksY][];

        for (int cx = 0; cx < chunksX; cx++) {
            for (int cy = 0; cy < chunksY; cy++) {
                chunks[cx][cy] = new WFCNode[chunkSize * chunkSize];
                // Initialize with nulls
                Arrays.fill(chunks[cx][cy], null);
            }
        }
        return chunks;
    }

    private void collapseChunk(WFCNode[] chunk, int chunkX, int chunkY) {
        // Here we process the 1D array as if it was 2D by calculating indices

        for (int i = 0; i < chunkSize; i++) {
            for (int j = 0; j < chunkSize; j++) {

                boolean chunkDone = false;
//                do {

                int globalX = chunkX * chunkSize + i;
                int globalY = chunkY * chunkSize + j;
                ArrayList<WFCNode> potentialNodes = getPotentialNodes(globalX, globalY);

                if (!potentialNodes.isEmpty()) {
                    WFCNode selectedNode = getRandomNode(potentialNodes);
                    int indexInChunk = i * chunkSize + j;
                    chunk[indexInChunk] = selectedNode;
                    map[globalX][globalY] = selectedNode; // Update the global map
                }else {
                    // If no potential nodes are found, it means ther is a configuration issue.
                    // Incrementally expand the recalcZone and try again.
                    checkAndResolveBoundaries(globalX, globalY);
                    }
//                } while (!chunkDone);
            }
        }
    }


    // check a potential empty tile and resolve the boundaries
    private void checkAndResolveBoundaries(int emptyTileX, int emptyTileY) {
        Zone recalcZone = calculateRecalculationZone(emptyTileX, emptyTileY);

        boolean isValid;
        do {
            for (int x = recalcZone.startX; x <= recalcZone.endX; x++) {
                for (int y = recalcZone.startY; y <= recalcZone.endY; y++) {
                    if (map[x][y] == null) {
                        ArrayList<WFCNode> potentialNodes = getPotentialNodesConsideringAllNeighbors(x, y);
                        if (!potentialNodes.isEmpty()) {
                            map[x][y] = getRandomNode(potentialNodes);
                        }
                    }
                }
            }
            isValid = validateRecalculationZone(recalcZone);
            if (!isValid) {
                recalcZone = expandRecalculationZone(recalcZone);
            }
        } while (!isValid && (recalcZone.startX > 0 || recalcZone.startY > 0 || recalcZone.endX < width - 1 || recalcZone.endY < Height - 1));

        if (!isValid) {
            System.err.println("Failed to resolve with maximum allowable zone.");
        }
    }

    // Get a list of potential nodes for a given position
    private ArrayList<WFCNode> getPotentialNodesConsideringAllNeighbors(int x, int y) {
        ArrayList<WFCNode> potentialNodes = new ArrayList<>(nodes); // Start with all possible nodes

        // Iterate over each direction to check the neighbors
        for (Vector2 offset : offsets) {
            int neighborX = x + (int) offset.x;
            int neighborY = y + (int) offset.y;

            // Ensure the neighbor is within grid boundaries
            if (IsInGrid(new Vector2(neighborX, neighborY))) {
                WFCNode neighborNode = map[neighborX][neighborY];

                if (neighborNode != null) {
                    // Reduce potential nodes based on the neighbor's compatible nodes
                    filterPotentialNodes(potentialNodes, neighborNode, offset);
                }
            }
        }

        return potentialNodes;
    }

    // filter potential nodes based on compatibility with a given neighbor
    private void filterPotentialNodes(ArrayList<WFCNode> potentialNodes, WFCNode neighborNode, Vector2 offset) {
        potentialNodes.removeIf(node -> !isCompatible(node, neighborNode, offset));
    }

    // Determines if two nodes are compatible based on their position relative to each other
    private boolean isCompatible(WFCNode node, WFCNode neighborNode, Vector2 offset) {
        if (offset.x == 0 && offset.y == 1) // Top neighbor
            return node.Top.compatibleNodes.contains(neighborNode);
        else if (offset.x == 0 && offset.y == -1) // Bottom neighbor
            return node.Bottom.compatibleNodes.contains(neighborNode);
        else if (offset.x == 1 && offset.y == 0) // Right neighbor
            return node.Right.compatibleNodes.contains(neighborNode);
        else if (offset.x == -1 && offset.y == 0) // Left neighbor
            return node.Left.compatibleNodes.contains(neighborNode);

        return false;
    }


    // get the zone around the empty tile that needs to be recalculated
    private Zone calculateRecalculationZone(int emptyTileX, int emptyTileY) {
        // Start by defining a basic zone around the empty tile
        int startX = Math.max(0, emptyTileX - 1);
        int startY = Math.max(0, emptyTileY - 1);
        int endX = Math.min(width - 1, emptyTileX + 1);
        int endY = Math.min(Height - 1, emptyTileY + 1);

        // Expand the zone until it encounters tiles or reaches the grid boundaries
        while (startX > 0 && isRowEmpty(startX - 1, startY, endY)) --startX;
        while (endX < width - 1 && isRowEmpty(endX + 1, startY, endY)) ++endX;
        while (startY > 0 && isColumnEmpty(startY - 1, startX, endX)) --startY;
        while (endY < Height - 1 && isColumnEmpty(endY + 1, startX, endX)) ++endY;

        return new Zone(startX, startY, endX, endY);
    }

    // Checks if a row is entirely empty within a specified range
    private boolean isRowEmpty(int row, int startCol, int endCol) {
        for (int i = startCol; i <= endCol; i++) {
            if (map[row][i] != null) return false;
        }
        return true;
    }

    // Checks if a column is entirely empty within a specified range
    private boolean isColumnEmpty(int col, int startRow, int endRow) {
        for (int i = startRow; i <= endRow; i++) {
            if (map[i][col] != null) return false;
        }
        return true;
    }


    // returns an expanded zone for recalculation
    private Zone expandRecalculationZone(Zone zone) {
        // Check if expansion is possible or makes sense
        if (zone.startX == 0 && zone.startY == 0 && zone.endX == width - 1 && zone.endY == Height - 1) {
            // Already at maximum size, no further expansion should happen
            return zone;
        }

        zone.startX = Math.max(0, zone.startX - 1);
        zone.startY = Math.max(0, zone.startY - 1);
        zone.endX = Math.min(width - 1, zone.endX + 1);
        zone.endY = Math.min(Height - 1, zone.endY + 1);

        System.out.printf("Expanded zone from (%d, %d)-(%d, %d)\n", zone.startX, zone.startY, zone.endX, zone.endY);
        return zone;
    }

    // chekcs if a zone is valid ( all the tiles are compatible with their neighbors)
    private boolean validateRecalculationZone(Zone zone) {
        for (int x = zone.startX; x <= zone.endX; x++) {
            for (int y = zone.startY; y <= zone.endY; y++) {
                WFCNode node = map[x][y];
                if (!isNodeCompatible(node, x, y)) {
                    System.err.println("Incompatibility found at (" + x + ", " + y + ")");
                    return false; // Configuration not valid
                }
            }
        }
        return true; // Configuration is valid
    }

    // takes a node and it's position and checks if it's compatible with it's neighbors
    private boolean isNodeCompatible(WFCNode node, int x, int y) {
        if (node == null) return false; // Empty node is not compatible with anything
        for (Vector2 offset : offsets) {

            int neighborX = x + (int) offset.x;
            int neighborY = y + (int) offset.y;
            if (IsInGrid(new Vector2(neighborX, neighborY))) {
                WFCNode neighborNode = map[neighborX][neighborY];
                if (neighborNode == null) return false;

                // Depending on the offset, check the compatibility in the corresponding direction
                if (offset.equals(new Vector2(0, 1)) && !node.Top.compatibleNodes.contains(neighborNode) ||
                        offset.equals(new Vector2(0, -1)) && !node.Bottom.compatibleNodes.contains(neighborNode) ||
                        offset.equals(new Vector2(1, 0)) && !node.Right.compatibleNodes.contains(neighborNode) ||
                        offset.equals(new Vector2(-1, 0)) && !node.Left.compatibleNodes.contains(neighborNode)) {
                    return false; // Found incompatibility
                }
            }
        }
        return true;
    }


    // helper class Zone
    private class Zone {
        public int startX, startY, endX, endY;

        public Zone(int startX, int startY, int endX, int endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }
    }



    // Main function to collapse the world
    // This function will divide the world into chunks and collapse each chunk
    // and then resolve the boundaries ( inefficient af )
    private void collapseWorld() {
        chunks = divideIntoChunks();
        for (int chunkX = 0; chunkX < width / chunkSize; chunkX++) {
            for (int chunkY = 0; chunkY < Height / chunkSize; chunkY++) {
                collapseChunk(chunks[chunkX][chunkY], chunkX, chunkY);
            }
        }

        for (int chunkX = 0; chunkX < width / chunkSize; chunkX++) {
            for (int chunkY = 0; chunkY < Height / chunkSize; chunkY++) {
                checkAndResolveBoundaries(chunkX, chunkY);
            }
        }


        // Final pass to check for any empty spaces and resolve if needed and add to outputTiles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < Height; y++) {
                if (map[x][y] != null) {
                        Tile tile = map[x][y].tile.clone((int) Math.round(map[x][y].tile.getId() + x + y * Math.random()));
                        tile.setIsReachable(!Objects.equals(map[x][y].name, "sandCenterWGrass[2][1]")); // Adjust reachability condition
                        tile.setPosition(new Vector2(x * width, y * Height));
                        outputTiles.add(tile);
                    }
                else {
                    // this means that there
                    recalculateZoneAroundEmptyTile(x, y);
                }
        }
}
    }

    // Recalculate the zone around an empty tile and resolve it
    private void recalculateZoneAroundEmptyTile(int emptyTileX, int emptyTileY) {
        int zoneSize = 1; // Start with the immediate neighbors
        boolean resolved = false;

        while (!resolved && zoneSize <= Math.max(width, Height)) {
            // Determine the zone to clear and recalculate
            int startX = Math.max(0, emptyTileX - zoneSize);
            int endX = Math.min(width - 1, emptyTileX + zoneSize);
            int startY = Math.max(0, emptyTileY - zoneSize);
            int endY = Math.min(Height - 1, emptyTileY + zoneSize);

            // Clear out the zone in the map and the outputTiles
            clearZone(startX, startY, endX, endY);

            // Try to resolve the zone
            resolved = fillZone(startX, startY, endX, endY);

            if (!resolved) {
                // If not resolved, expand the zone
                zoneSize++;
            } else {
                updateOutputTiles(startX, startY, endX, endY);
            }
        }

        if (!resolved) {
            System.err.println("Failed to resolve even after expanding the zone to maximum size.");
            // At this point, we have exhausted all possibilities and the world cannot be resolved
            // we handle this by choosing a random tile for the empty space
            // and we will choose the tile that is the most repeated
            // if there are no repeated tiles we will choose a random tile
            Map<Integer, Integer> repeatedTiles = new HashMap<>();
            for (int i = 0; i < offsets.length; i++) {
                Vector2 neighbor = new Vector2(emptyTileX + offsets[i].x, emptyTileY + offsets[i].y);
                if (IsInGrid(neighbor)) {
                    WFCNode neighborNode = map[(int) neighbor.x][(int) neighbor.y];
                    if (neighborNode != null) {
                        int tileId = neighborNode.tile.getId();
                        if (repeatedTiles.containsKey(tileId)) {
                            repeatedTiles.put(tileId, repeatedTiles.get(tileId) + 1);
                        } else {
                            repeatedTiles.put(tileId, 1);
                        }
                    }
                }
            }

            // if there are no repeated tiles we will choose a random tile
            if (repeatedTiles.isEmpty()) {
                WFCNode newNode = getRandomNode(nodes);
                map[emptyTileX][emptyTileY] = newNode;
                // chose according to the entropy
                Tile tile = newNode.tile.clone((int) Math.round(newNode.tile.getId() + emptyTileX + emptyTileY * Math.random()));
                tile.setIsReachable(!Objects.equals(newNode.name, "sandCenterWGrass[2][1]")); // Adjust reachability condition
                tile.setPosition(new Vector2(emptyTileX * width, emptyTileY * Height));
                outputTiles.add(tile);
            } else {
                // we will choose the tile that is the most repeated
                int max = 0;
                int tileId = 0;
                for (Map.Entry<Integer, Integer> entry : repeatedTiles.entrySet()) {
                    if (entry.getValue() > max) {
                        max = entry.getValue();
                        tileId = entry.getKey();
                    }
                }
                int finalTileId = tileId;
                WFCNode newNode = nodes.stream().filter(node -> node.tile.getId() == finalTileId).findFirst().orElse(null);
                map[emptyTileX][emptyTileY] = newNode;
                Tile tile = newNode.tile.clone((int) Math.round(newNode.tile.getId() + emptyTileX + emptyTileY * Math.random()));
                tile.setIsReachable(!Objects.equals(newNode.name, "sandCenterWGrass[2][1]")); // Adjust reachability condition
                tile.setPosition(new Vector2(emptyTileX * width, emptyTileY * Height));
                outputTiles.add(tile);
            }

        }
    }

    private void clearZone(int startX, int startY, int endX, int endY) {
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                map[x][y] = null;
            }
        }
        outputTiles.removeIf(tile ->
                tile.getPosition().x >= startX && tile.getPosition().x <= endX &&
                        tile.getPosition().y >= startY && tile.getPosition().y <= endY
        );
    }

    private boolean fillZone(int startX, int startY, int endX, int endY) {
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                ArrayList<WFCNode> potentialNodes = getPotentialNodesConsideringAllNeighbors(x, y);
                if (!potentialNodes.isEmpty()) {
                    WFCNode selectedNode = getRandomNode(potentialNodes);
                    map[x][y] = selectedNode; // Update the map with the selected node
                } else {
                    return false; // Return false if no compatible nodes found, indicating unresolved zone
                }
            }
        }
        return true; // Return true if the entire zone is filled successfully
    }

    private void updateOutputTiles(int startX, int startY, int endX, int endY) {
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (map[x][y] != null) {
                    Tile tile = map[x][y].tile.clone((int) Math.round(map[x][y].tile.getId() + x + y * Math.random()));
                    tile.setIsReachable(!Objects.equals(map[x][y].name, "sandCenterWGrass[2][1]"));
                    tile.setPosition(new Vector2(x * width, y * Height));
                    outputTiles.add(tile); // Add the tile to the output list
                }
            }
        }
    }

    // Method to get potential nodes based on the neighbors and their neighbors
    private ArrayList<WFCNode> getPotentialNodes(int x, int y) {
        ArrayList<WFCNode> potentialNodes = new ArrayList<>(nodes);

        // Iterate through neighbors and update potentialNodes
        for (int i = 0; i < offsets.length; i++) {
            Vector2 neighbor = new Vector2(x + offsets[i].x, y + offsets[i].y);
            if (IsInGrid(neighbor)) {
                WFCNode neighborNode = map[(int) neighbor.x][(int) neighbor.y];
                if (neighborNode != null) {
                    switch (i) {
                        case 0:
                            whittleNodes(potentialNodes, neighborNode.Bottom.compatibleNodes);
                            break;
                        case 1:
                            whittleNodes(potentialNodes, neighborNode.Top.compatibleNodes);
                            break;
                        case 2:
                            whittleNodes(potentialNodes, neighborNode.Left.compatibleNodes);
                            break;
                        case 3:
                            whittleNodes(potentialNodes, neighborNode.Right.compatibleNodes);
                            break;
                    }
                }
            }
        }

        return potentialNodes;
    }


    // Method to get a random node from a list of nodes based on entropy
    private WFCNode getRandomNode(ArrayList<WFCNode> nodes) {
        double totalEntropy = nodes.stream().mapToDouble(node -> node.entropy).sum();
        double randomValue = Math.random() * totalEntropy;

        for (WFCNode node : nodes) {
            randomValue -= node.entropy;
            if (randomValue <= 0) {
                return node;
            }
        }

        return nodes.get((int) (Math.random() * nodes.size()));
    }

    // Method to check if coordinates are within the grid bounds
    private boolean IsInGrid(Vector2 coords) {
        return coords.x >= 0 && coords.x < width && coords.y >= 0 && coords.y < Height;
    }


    // get the output tiles
    public ArrayList<Tile> getOutputTiles() {
        return outputTiles;
    }


    // compares a list of potential nodes to a lmist of a valid nodes and remove
    // all non valid nodes from the potential nodes
    private void whittleNodes(ArrayList<WFCNode> potentialNodes, ArrayList<WFCNode> validNodes ) {
        for (int i = 0; i < potentialNodes.size(); i++) {
            if (!validNodes.contains(potentialNodes.get(i))) {
                potentialNodes.remove(i);
                i--;
            }
        }
    }


}