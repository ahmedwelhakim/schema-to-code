package com.github.ahmedwelhakim.schematocode.plugin

import com.github.ahmedwelhakim.schematocode.plugin.emit.typescript.TypescriptEmitter
import com.github.ahmedwelhakim.schematocode.plugin.infer.inferFromJson
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class JsonToTypeScriptTest {

    @Test
    fun `test simple JSON object conversion`() {
        val json = """
            {
                "name": "John",
                "age": 30,
                "isActive": true
            }
        """.trimIndent()
        
        val jsonElement = Json.parseToJsonElement(json)
        val typeDef = inferFromJson("User", jsonElement as kotlinx.serialization.json.JsonObject)
        val typescriptCode = TypescriptEmitter().emit(typeDef)
        
        // Just test that it contains the expected content
        assert(typescriptCode.contains("export interface User {"))
        assert(typescriptCode.contains("name: string;"))
        assert(typescriptCode.contains("age: number;"))
        assert(typescriptCode.contains("isActive: boolean;"))
        assert(typescriptCode.contains("}"))
    }

    @Test
    fun `test nested object conversion`() {
        val json = """
            {
                "user": {
                    "name": "John",
                    "address": {
                        "city": "New York",
                        "country": "USA"
                    }
                }
            }
        """.trimIndent()
        
        val jsonElement = Json.parseToJsonElement(json)
        val typeDef = inferFromJson("RootObject", jsonElement as kotlinx.serialization.json.JsonObject)
        val typescriptCode = TypescriptEmitter().emit(typeDef)
        
        // Test that it contains the expected content
        assert(typescriptCode.contains("export interface RootObject {"))
        assert(typescriptCode.contains("user: {"))
        assert(typescriptCode.contains("name: string;"))
        assert(typescriptCode.contains("address: {"))
        assert(typescriptCode.contains("city: string;"))
        assert(typescriptCode.contains("country: string;"))
        assert(typescriptCode.contains("}"))
    }

    @Test
    fun `test array conversion`() {
        val json = """
            {
                "tags": ["tag1", "tag2"],
                "scores": [1, 2, 3]
            }
        """.trimIndent()
        
        val jsonElement = Json.parseToJsonElement(json)
        val typeDef = inferFromJson("Data", jsonElement as kotlinx.serialization.json.JsonObject)
        val typescriptCode = TypescriptEmitter().emit(typeDef)
        
        // Test that it contains the expected content
        assert(typescriptCode.contains("export interface Data {"))
        assert(typescriptCode.contains("tags: string[];"))
        assert(typescriptCode.contains("scores: number[];"))
        assert(typescriptCode.contains("}"))
    }
}
