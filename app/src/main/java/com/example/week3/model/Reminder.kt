package com.example.week3.model

class Reminder (
    var id: String,
    var title: String,
    var description: String,
    var category: Category,
    var dateTime: String, // ISO Date String
    var isUrgent: Boolean
)