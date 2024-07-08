package com.orphee.hello_spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.util.UUID
import org.springframework.jdbc.core.query
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.http.ResponseEntity
import kotlin.jvm.optionals.toList
import java.util.*
import java.net.http.HttpResponse
import java.net.URI

@SpringBootApplication
class HelloSpringApplication

fun main(args: Array<String>) {
	runApplication<HelloSpringApplication>(*args)
}

@Table("MESSAGES")
data class Message(@Id var id: String? , val text: String) 

interface MessageRepository: CrudRepository<Message, String>

@RestController
class MessageController(val service: MessageService){
	@GetMapping("/")
	fun index() = service.findMessages()

 

	@PostMapping("/")
	fun post(@RequestBody message: Message): ResponseEntity<Message>
	{
		service.save(message)

		return ResponseEntity.created(URI("/${message.id}")).body(message)
	}

 

	@GetMapping("/{id}")
	fun index(@PathVariable id: String):ResponseEntity<Message>
 	{
		val mayBeMessage = service.findMessageById(id)

		if(!mayBeMessage.isPresent)  {
			return ResponseEntity.notFound().build()
		}
		
		return ResponseEntity.ok(mayBeMessage.get())

	}

}



@Service
class MessageService(val db: MessageRepository){
	fun findMessages(): List<Message> = db.findAll().toList()

	fun findMessageById(id: String): Optional<Message> = db.findById(id)

	fun save(message: Message) {
		db.save(message)
	}

}