class Drug {
    private int id;
    private String name;
    private int categoryId;
    private String description;
    private int count;
    private int providerId;

    public Drug(int id, String name, int categoryId, String description, int count, int providerId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
        this.count = count;
        this.providerId = providerId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public int getProviderId() {
        return providerId;
    }
}