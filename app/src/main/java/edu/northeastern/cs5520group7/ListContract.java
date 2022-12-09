package edu.northeastern.cs5520group7;

public interface ListContract {

    interface Model{
        int COLLECTION_COUNT = 1;
        int[] BOOK_SIZE = {175*3, 280*3};

        void fetchAllBooks(User user, ListContract.View view, ListContract.Presenter presenter);

    }
}
