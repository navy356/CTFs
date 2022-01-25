class Simple {
    public static void main(String args[]) {
        System.out.println("Hello Java");
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", "echo \"some text\" >test");
    }
}