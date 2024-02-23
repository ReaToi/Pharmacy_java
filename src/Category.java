class Category {
    private int id;
    private String name;
    private int categoryTypeId;

    public Category(int id, String name, int categoryTypeId) {
        this.id = id;
        this.name = name;
        this.categoryTypeId = categoryTypeId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryTypeId() {
        return categoryTypeId;
    }
}