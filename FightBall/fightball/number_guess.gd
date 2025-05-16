extends Node2D

var wsUrl = "ws://localhost:8080/chat"
var socket := WebSocketPeer.new()
var handshake_headers= PackedStringArray(["Authorization: Bearer " + PlayerInfo.userKey])

func _ready() -> void:
	set_meta("scene_path", "res://numberGuess.tscn")
	
	socket.handshake_headers=handshake_headers
	var err = socket.connect_to_url(wsUrl)
	if err != OK:
		print("❌ Error al conectar:", err)
		set_process(false)
	else:
		print("⌛ Conectando...")

func _process(delta):
	socket.poll()

	if socket.get_ready_state() == WebSocketPeer.STATE_OPEN:
		print("✅ ¡Conexión establecida!")
		socket.send_text("Test packet")

	if socket.get_available_packet_count() > 0:
		var packet = socket.get_packet().get_string_from_utf8()
		print("📩 Paquete recibido:", packet)
		
func _exit_tree():
	if socket.get_ready_state() == WebSocketPeer.STATE_OPEN:
		socket.close(1000, "Scene exit")

func _on_go_back_pressed() -> void:
	SceneManager.go_back() # Replace with function body.
