package models;

    public class ListView{
        int id;
        String name;
        Boolean simple;

        public ListView(int id, String name, Boolean simple) {
            this.id = id;
            this.name = name;
            this.simple = simple;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Boolean getSimple() {
            return simple;
        }
    }

