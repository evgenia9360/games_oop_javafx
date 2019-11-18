package ru.job4j.puzzle;

import ru.job4j.puzzle.firuges.Cell;
import ru.job4j.puzzle.firuges.Figure;

import java.util.Arrays;

/**
 * Реализация игры Сокобан.
 */
public class Logic {
    private final int size;
    private final Figure[] figures;
    private int index = 0;

    /**
     * Конструктор.
     *
     * @param size размер игрового поля.
     */
    public Logic(int size) {
        this.size = size;
        this.figures = new Figure[size * size];
    }

    /**
     * Добавление новой фигуры.
     *
     * @param figure фигура.
     */
    public void add(Figure figure) {
        this.figures[this.index++] = figure;
    }

    /**
     * Перемещение из одной ячейки в другую.
     *
     * @param source начальная ячейка.
     * @param dest   конечная ячейка.
     * @return результат перемещения. Если значение начальной ячейки null - false.
     */
    public boolean move(Cell source, Cell dest) {
        boolean rst = false;
        int index = this.findBy(source);
        if (index != -1) {
            Cell[] steps = this.figures[index].way(source, dest);
            if (this.isFree(steps)) {
                rst = true;
                this.figures[index] = this.figures[index].copy(dest);
            }
        }
        return rst;
    }

    /**
     * Проверяем массив ячеек.
     *
     * @param cells ячейки.
     * @return true если значение всех ячеек равно null, иначе false.
     */
    public boolean isFree(Cell... cells) {
        boolean result = cells.length > 0;
        for (Cell cell : cells) {
            if (this.findBy(cell) != -1) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Присваивает значение null каждому элементу переданного массива.
     * Присваивает значение 0 индексу.
     */
    public void clean() {
        Arrays.fill(this.figures, null);
        this.index = 0;
    }

    /**
     * Найти по ячейке индекс.
     *
     * @param cell ячейка.
     * @return индекс ячейки.
     */
    private int findBy(Cell cell) {
        int rst = -1;
        for (int index = 0; index != this.figures.length; index++) {
            if (this.figures[index] != null && this.figures[index].position().equals(cell)) {
                rst = index;
                break;
            }
        }
        return rst;
    }

    /**
     * Проверка выигрышных комбинаций.
     *
     * @return результат проверки.
     */
    public boolean isWin() {
        int[][] table = this.convert();
        boolean result = false;
        for (int row = 0; row < table.length; row++) {
            int count = 0;
            int count2 = 0;
            for (int cell = 0; cell < table.length; cell++) {
                int sign = table[row][cell];
                int sign2 = table[cell][row];
                if (sign == 1) {
                    count++;
                }
                if (sign2 == 1) {
                    count2++;
                }
            }

            if (count == table.length || count2 == table.length) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Создание матрицы.
     *
     * @return матрица.
     */
    public int[][] convert() {
        int[][] table = new int[this.size][this.size];
        for (int row = 0; row != table.length; row++) {
            for (int cell = 0; cell != table.length; cell++) {
                int position = this.findBy(new Cell(row, cell));
                if (position != -1 && this.figures[position].movable()) {
                    table[row][cell] = 1;
                }
            }
        }
        return table;
    }

    /**
     * Преобразование матрицы в строковое представление.
     *
     * @return строковое представление матрицы.
     */
    @Override
    public String toString() {
        return Arrays.toString(this.convert());
    }
}
