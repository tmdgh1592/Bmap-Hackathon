package com.app.hackathon.domain.entity

import android.os.Parcel
import android.os.Parcelable

data class FilterOption(
    var optionName: String,
    var isChecked: Int
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readInt()) {
        optionName = parcel.readString()!!
        isChecked = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(optionName)
    }

    companion object CREATOR : Parcelable.Creator<FilterOption> {
        override fun createFromParcel(parcel: Parcel): FilterOption {
            return FilterOption(parcel)
        }

        override fun newArray(size: Int): Array<FilterOption?> {
            return arrayOfNulls(size)
        }
    }
}