extends GridContainer

@export var character_slot: PackedScene
@export var characters: Array[Character]
signal characterSelected;
func _ready():
	load_characters()

func load_characters():
	for character in characters:
		var slot = character_slot.instantiate()
		slot.icon = character.icon
		slot.pressed.connect(_on_character_pressed.bind(character))
		add_child(slot)

func _on_character_pressed(character: Character):
	Persistence.character=character
	Persistence.charname1=character.name
	emit_signal("characterSelected")
