<?xml version="1.0" encoding="UTF-8"?>
<tileset name="commands" tilewidth="32" tileheight="32" tilecount="12" columns="4">
 <image source="commands.png" width="144" height="96"/>
 <tile id="0">
  <properties>
   <property name="command">[believe.character.playable.proto.PlayableCharacterMovementCommand.playable_character_movement_command] {
  action: MOVE_RIGHT
}</property>
   <property name="entity_type" value="command"/>
   <property name="should_despawn" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="1">
  <properties>
   <property name="command">[believe.character.playable.proto.PlayableCharacterMovementCommand.playable_character_movement_command] {
  action: MOVE_LEFT
}</property>
   <property name="entity_type" value="command"/>
   <property name="should_despawn" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="4">
  <properties>
   <property name="command">[believe.character.playable.proto.PlayableCharacterMovementCommand.playable_character_movement_command] {
  action: STOP
}</property>
   <property name="entity_type" value="command"/>
   <property name="should_despawn" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="5">
  <properties>
   <property name="command">[believe.character.playable.proto.PlayableCharacterMovementCommand.playable_character_movement_command] {
  action: JUMP
}</property>
   <property name="entity_type" value="command"/>
   <property name="should_despawn" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="8">
  <properties>
   <property name="command" value="dialogue"/>
   <property name="entity_type" value="command"/>
   <property name="should_despawn" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="9">
  <properties>
   <property name="command" value="sequence"/>
   <property name="entity_type" value="command"/>
   <property name="should_despawn" type="bool" value="true"/>
  </properties>
 </tile>
</tileset>
