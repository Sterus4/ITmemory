package ru.sterus.history.itmemory.model

import android.os.Parcel
import android.os.Parcelable

data class Article(val title: String, val image: String, val content: String, val date: String, val description: String, val wikipediaURL: String, val yandexMapsURl: String, val section: String, val photos: ArrayList<Photos>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readArrayList(Photos::class.java.classLoader) as ArrayList<Photos>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(content)
        parcel.writeString(date)
        parcel.writeString(description)
        parcel.writeString(wikipediaURL)
        parcel.writeString(yandexMapsURl)
        parcel.writeString(section)
        parcel.writeList(photos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }


}