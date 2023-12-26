package com.cs;

import java.util.ArrayList;

public class PetriNet {
    /**
     * 创建关联矩阵
     */
    public int[][] creatIncidenceMatrix(){
        //关联矩阵，可以从此得到各库所的前集和后集。行代表库所，列代表变迁，库所P的的前集为1、后集为-1，变迁T的前集为-1、后集为1
        int[][] incidenceMatrix = new int[29][64];
        for (int i = 0; i < 29; i++) {
            for (int j = 0; j < 64; j++) {
                incidenceMatrix[i][j] = 0;
            }
        }
        //设置各个库所的前集和后集
        incidenceMatrix[0][1] = 1;
        incidenceMatrix[0][9] = 1;
        incidenceMatrix[0][0] = -1;
        incidenceMatrix[0][8] = -1;

        incidenceMatrix[1][0] = 1;
        incidenceMatrix[1][3] = 1;
        incidenceMatrix[1][49] = 1;
        incidenceMatrix[1][1] = -1;
        incidenceMatrix[1][2] = -1;
        incidenceMatrix[1][48] = -1;

        incidenceMatrix[2][2] = 1;
        incidenceMatrix[2][5] = 1;
        incidenceMatrix[2][41] = 1;
        incidenceMatrix[2][3] = -1;
        incidenceMatrix[2][4] = -1;
        incidenceMatrix[2][40] = -1;

        incidenceMatrix[3][4] = 1;
        incidenceMatrix[3][7] = 1;
        incidenceMatrix[3][51] = 1;
        incidenceMatrix[3][5] = -1;
        incidenceMatrix[3][6] = -1;
        incidenceMatrix[3][50] = -1;

        incidenceMatrix[4][6] = 1;
        incidenceMatrix[4][17] = 1;
        incidenceMatrix[4][7] = -1;
        incidenceMatrix[4][16] = -1;

        incidenceMatrix[5][8] = 1;
        incidenceMatrix[5][11] = 1;
        incidenceMatrix[5][57] = 1;
        incidenceMatrix[5][9] = -1;
        incidenceMatrix[5][10] = -1;
        incidenceMatrix[5][56] = -1;

        incidenceMatrix[6][10] = 1;
        incidenceMatrix[6][13] = 1;
        incidenceMatrix[6][25] = 1;
        incidenceMatrix[6][11] = -1;
        incidenceMatrix[6][12] = -1;
        incidenceMatrix[6][24] = -1;

        incidenceMatrix[7][12] = 1;
        incidenceMatrix[7][15] = 1;
        incidenceMatrix[7][59] = 1;
        incidenceMatrix[7][13] = -1;
        incidenceMatrix[7][14] = -1;
        incidenceMatrix[7][58] = -1;

        incidenceMatrix[8][14] = 1;
        incidenceMatrix[8][33] = 1;
        incidenceMatrix[8][15] = -1;
        incidenceMatrix[8][32] = -1;

        incidenceMatrix[9][16] = 1;
        incidenceMatrix[9][19] = 1;
        incidenceMatrix[9][61] = 1;
        incidenceMatrix[9][17] = -1;
        incidenceMatrix[9][18] = -1;
        incidenceMatrix[9][60] = -1;

        incidenceMatrix[10][18] = 1;
        incidenceMatrix[10][21] = 1;
        incidenceMatrix[10][30] = 1;
        incidenceMatrix[10][19] = -1;
        incidenceMatrix[10][20] = -1;
        incidenceMatrix[10][31] = -1;

        incidenceMatrix[11][20] = 1;
        incidenceMatrix[11][23] = 1;
        incidenceMatrix[11][63] = 1;
        incidenceMatrix[11][21] = -1;
        incidenceMatrix[11][22] = -1;
        incidenceMatrix[11][62] = -1;

        incidenceMatrix[12][22] = 1;
        incidenceMatrix[12][38] = 1;
        incidenceMatrix[12][23] = -1;
        incidenceMatrix[12][39] = -1;

        incidenceMatrix[13][24] = 1;
        incidenceMatrix[13][27] = 1;
        incidenceMatrix[13][25] = -1;
        incidenceMatrix[13][26] = -1;

        incidenceMatrix[14][26] = 1;
        incidenceMatrix[14][29] = 1;
        incidenceMatrix[14][42] = 1;
        incidenceMatrix[14][45] = 1;
        incidenceMatrix[14][27] = -1;
        incidenceMatrix[14][28] = -1;
        incidenceMatrix[14][43] = -1;
        incidenceMatrix[14][44] = -1;

        incidenceMatrix[15][28] = 1;
        incidenceMatrix[15][31] = 1;
        incidenceMatrix[15][29] = -1;
        incidenceMatrix[15][30] = -1;

        incidenceMatrix[16][32] = 1;
        incidenceMatrix[16][35] = 1;
        incidenceMatrix[16][52] = 1;
        incidenceMatrix[16][33] = -1;
        incidenceMatrix[16][34] = -1;
        incidenceMatrix[16][53] = -1;

        incidenceMatrix[17][34] = 1;
        incidenceMatrix[17][37] = 1;
        incidenceMatrix[17][46] = 1;
        incidenceMatrix[17][35] = -1;
        incidenceMatrix[17][36] = -1;
        incidenceMatrix[17][47] = -1;

        incidenceMatrix[18][36] = 1;
        incidenceMatrix[18][39] = 1;
        incidenceMatrix[18][54] = 1;
        incidenceMatrix[18][37] = -1;
        incidenceMatrix[18][38] = -1;
        incidenceMatrix[18][55] = -1;

        incidenceMatrix[19][40] = 1;
        incidenceMatrix[19][43] = 1;
        incidenceMatrix[19][41] = -1;
        incidenceMatrix[19][42] = -1;

        incidenceMatrix[20][44] = 1;
        incidenceMatrix[20][47] = 1;
        incidenceMatrix[20][45] = -1;
        incidenceMatrix[20][46] = -1;

        incidenceMatrix[21][48] = 1;
        incidenceMatrix[21][49] = -1;

        incidenceMatrix[22][50] = 1;
        incidenceMatrix[22][51] = -1;

        incidenceMatrix[23][53] = 1;
        incidenceMatrix[23][52] = -1;

        incidenceMatrix[24][55] = 1;
        incidenceMatrix[24][54] = -1;

        incidenceMatrix[25][56] = 1;
        incidenceMatrix[25][57] = -1;

        incidenceMatrix[26][58] = 1;
        incidenceMatrix[26][59] = -1;

        incidenceMatrix[27][60] = 1;
        incidenceMatrix[27][61] = -1;

        incidenceMatrix[28][62] = 1;
        incidenceMatrix[28][63] = -1;

        return incidenceMatrix;
    }

    /**
     * 获得库所P的后集变迁索引集合
     * @param index
     * @param matrix
     * @return
     */
    public ArrayList<Integer> getBackwardPlaceIndexSetForPlace(int index , int[][] matrix) {
        //找出P的后集变迁索引集合
        ArrayList<Integer> arrT = new ArrayList<>();
        for (int i = 0; i < matrix[index].length; i++) {
            if (matrix[index][i] == -1){
                arrT.add(i);
            }
        }
        //进一步找出P的后集库所集合
        ArrayList<Integer> arrP = new ArrayList<>();
        for (Integer item:arrT) {
            for (int i = 0; i < matrix.length; i++) {
                if( matrix[i][item] == 1){
                    arrP.add(i);
                }
            }
        }
        return arrP;
    }

    /**
     * 获得变迁T的前集和后集
     * @param index
     * @param matrix
     * @return
     */
    public int[] getPlaceIndexForTransition(int index ,int[][] matrix){
        int[] arr = new int[2];
        for (int i = 0; i < matrix.length; i++) {
                if( matrix[i][index] == -1){
                    arr[0] = i;//前集库所索引
                }else if(matrix[i][index] == 1){
                    arr[1] = i;//后集库所索引
                }
        }

        return arr;
    }

    /**
     * 创建所有的库所
     * @return
     */
    public Place[] creatPlace(){
        Place[] places = new Place[29];
        for (int i = 0; i < 29; i++) {
            places[i] = new Place(i,'P'+ Integer.toString(i),null,0,0,0,0,0,null,getBackwardPlaceIndexSetForPlace(i,creatIncidenceMatrix()));
        }
        //建立库所坐标
        places[0].coord = new Coord(1,1);
        places[1].coord = new Coord(2,1);
        places[2].coord = new Coord(3,1);
        places[3].coord = new Coord(4,1);
        places[4].coord = new Coord(5,1);
        places[5].coord = new Coord(1,2);
        places[6].coord = new Coord(1,3);
        places[7].coord = new Coord(1,4);
        places[8].coord = new Coord(1,5);
        places[9].coord = new Coord(5,2);
        places[10].coord = new Coord(5,3);
        places[11].coord = new Coord(5,4);
        places[12].coord = new Coord(5,5);
        places[13].coord = new Coord(2,3);
        places[14].coord = new Coord(3,3);
        places[15].coord = new Coord(4,3);
        places[16].coord = new Coord(2,5);
        places[17].coord = new Coord(3,5);
        places[18].coord = new Coord(4,5);
        places[19].coord = new Coord(3,2);
        places[20].coord = new Coord(3,4);
        places[21].coord = new Coord(2,0);
        places[22].coord = new Coord(4,0);
        places[23].coord = new Coord(2,6);
        places[24].coord = new Coord(4,6);
        places[25].coord = new Coord(0,2);
        places[26].coord = new Coord(0,4);
        places[27].coord = new Coord(6,2);
        places[28].coord = new Coord(6,4);


        return places;
    }

    /**
     * 创建所有的变迁
     * @return
     */
    public Transition[] creatTransition(){
        Transition[] transitions = new Transition[64];
        for (int i = 0;i < 64; i++){
            transitions[i] = new Transition(i,'T'+ Integer.toString(i),1,getPlaceIndexForTransition(i ,creatIncidenceMatrix())[0],getPlaceIndexForTransition(i ,creatIncidenceMatrix())[1],false,false);
        }
        return transitions;
    }
}
