import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class linkPriceCodingTest {
//    public static void main(String[] args) {
//        // 모든 문자열에 공통으로 포함된 가장 긴 접두사를 반환하는 함수
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        bufferedReader
//
//        // 문자 배열 input
//된 광고입니다.
//        // 배열
//    }

    public static class DynamicArray {
        private int[] array;
        private int size;

        public DynamicArray() {
            array = new int[2];
            size = 0;
        }

        public void add(int element) {
            if (size == array.length) {
                resize();
            }
            array[size++] = element;
        }

        private void resize() {
            int newSize = array.length * 2;
            array = Arrays.copyOf(array, newSize);
        }

        public int get(int index) {
            if (index >= size || index < 0) {
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
            return array[index];
        }

        public int size() {
            return size;
        }

        public static void main(String[] args) {
            DynamicArray dynamicArray = new DynamicArray();
            dynamicArray.add(1);
            dynamicArray.add(2);
            dynamicArray.add(3);  // 배열 크기가 자동으로 증가

            for (int i = 0; i < dynamicArray.size(); i++) {
                System.out.println(dynamicArray.get(i));
            }
        }
    }
}
