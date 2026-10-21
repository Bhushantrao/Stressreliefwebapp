import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { JournalService } from '../../services/journal.service';
import { JournalEntry } from '../../models/models';

@Component({
  selector: 'app-journal',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './journal.component.html',
  styleUrls: ['./journal.component.css']
})
export class JournalComponent implements OnInit {
  journals: JournalEntry[] = [];
  searchQuery = '';
  isLoading = true;

  // Editor Form Modal State
  isEditorOpen = false;
  editingId: number | null = null;
  formTitle = '';
  formContent = '';
  formMoodTag = 'CALM';
  formTags = '';
  isSaving = false;
  editorError = '';

  // View Entry Modal State
  selectedEntry: JournalEntry | null = null;

  moodTags = ['HAPPY', 'CALM', 'GRATEFUL', 'NEUTRAL', 'ANXIOUS', 'REFLECTIVE', 'STRESSED'];

  constructor(private journalService: JournalService) {}

  ngOnInit(): void {
    this.loadJournals();
  }

  loadJournals(): void {
    this.isLoading = true;
    this.journalService.getJournals(this.searchQuery).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.success && res.data) {
          this.journals = res.data;
        }
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  onSearch(): void {
    this.loadJournals();
  }

  openCreateModal(): void {
    this.editingId = null;
    this.formTitle = '';
    this.formContent = '';
    this.formMoodTag = 'CALM';
    this.formTags = '';
    this.editorError = '';
    this.isEditorOpen = true;
  }

  openEditModal(entry: JournalEntry): void {
    if (!entry.id) return;
    this.editingId = entry.id;
    this.formTitle = entry.title;
    this.formContent = entry.content;
    this.formMoodTag = entry.moodTag || 'CALM';
    this.formTags = entry.tags || '';
    this.editorError = '';
    this.isEditorOpen = true;
  }

  closeEditor(): void {
    this.isEditorOpen = false;
  }

  viewEntry(entry: JournalEntry): void {
    this.selectedEntry = entry;
  }

  closeViewer(): void {
    this.selectedEntry = null;
  }

  saveJournal(): void {
    if (!this.formTitle.trim() || !this.formContent.trim()) {
      this.editorError = 'Please provide both title and content for your journal entry';
      return;
    }

    this.isSaving = true;
    this.editorError = '';

    const payload = {
      title: this.formTitle.trim(),
      content: this.formContent.trim(),
      moodTag: this.formMoodTag,
      tags: this.formTags.trim()
    };

    if (this.editingId) {
      this.journalService.updateJournal(this.editingId, payload).subscribe({
        next: (res) => {
          this.isSaving = false;
          if (res.success) {
            this.closeEditor();
            this.loadJournals();
          }
        },
        error: (err) => {
          this.isSaving = false;
          this.editorError = err.error?.message || 'Failed to update journal entry';
        }
      });
    } else {
      this.journalService.createJournal(payload).subscribe({
        next: (res) => {
          this.isSaving = false;
          if (res.success) {
            this.closeEditor();
            this.loadJournals();
          }
        },
        error: (err) => {
          this.isSaving = false;
          this.editorError = err.error?.message || 'Failed to create journal entry';
        }
      });
    }
  }

  deleteJournal(entry: JournalEntry, event?: Event): void {
    if (event) event.stopPropagation();
    if (!entry.id) return;

    if (confirm(`Are you sure you want to delete "${entry.title}"? This cannot be undone.`)) {
      this.journalService.deleteJournal(entry.id).subscribe({
        next: () => {
          if (this.selectedEntry?.id === entry.id) {
            this.selectedEntry = null;
          }
          this.loadJournals();
        }
      });
    }
  }
}
